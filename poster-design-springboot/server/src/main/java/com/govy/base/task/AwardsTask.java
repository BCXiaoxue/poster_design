package com.govy.base.task;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.google.common.collect.Maps;
import com.govy.base.mapper.AwardMapper;
import com.govy.base.mapper.CompetitionMapper;
import com.govy.base.mapper.ScoreMapper;
import com.govy.base.mapper.TeamMapper;
import com.govy.base.service.AwardService;
import com.govy.base.service.CompetitionService;
import com.govy.base.service.TeamService;
import com.govy.common.constant.ResultConstant;
import com.govy.common.constant.TableConstant;
import com.govy.common.properties.CertificateGeneratorProperties;
import com.govy.common.utils.CertificateGeneratorUtil;
import com.govy.common.utils.MinioUtil;
import com.govy.common.utils.ThumbFileUtil;
import com.govy.model.entity.Award;
import com.govy.model.entity.Competition;
import com.govy.model.entity.Team;
import com.govy.model.vo.TeamWithScoreVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.First;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 定时处理结束竞赛的颁奖
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AwardsTask {

    private final CompetitionMapper competitionMapper;
    private final CompetitionService competitionService;
    private final ScoreMapper scoreMapper;
    private final AwardService awardService;
    private final TeamService teamService;
    private final AwardMapper awardMapper;
    private final TeamMapper teamMapper;
    private final MinioUtil minioUtil;

    /**
     * 定时处理结束竞赛的颁奖
     */
    @Transactional
    @Scheduled(cron = "0 0 1 * * ?")  // 每天凌晨1点触发一次
//    @Scheduled(cron = "* * * * * ?")  // 每天凌晨1点触发一次
    @SneakyThrows
    public void processDeliveryOrder() {

        log.info("定时处理结束竞赛的颁奖开始, {}", LocalDateTime.now());

        // 1. 获取已经结束，且没有颁奖的竞赛id
        List<Integer> competitionIds = competitionMapper.selectEndCompetition(TableConstant.UNAWARDS);

        // 2. 获取这些竞赛的队伍的评分
        for (Integer competitionId : competitionIds) {
            // 3.查询这些竞赛的队伍的评分
            List<TeamWithScoreVO> list = scoreMapper.selectTeamScore(competitionId);
            System.out.println("list: " + list);
            // 4.遍历这些队伍的评分, 计算每个队伍的总分数
            for (TeamWithScoreVO teamWithScoreVO : list) {
                // 4.1 获取队伍id
                Integer teamId = teamWithScoreVO.getTeamId();
                // 4.2 解锁这些参赛队伍
                teamService.update(new LambdaUpdateWrapper<Team>()
                        .set(Team::getTeamStatus, TableConstant.TEAM_UNLOCKED)
                        .eq(Team::getId, teamId)
                );

                // 5.获取队伍id, 和这个队伍对应竞赛的评分
                List<Integer> scoreList = teamWithScoreVO.getScoreList();
                if (CollUtil.isEmpty(scoreList)) {
                    continue;
                }
                // 5.1 设置总分初始值为0
                teamWithScoreVO.setTotalScore(0);
                scoreList.forEach(score -> teamWithScoreVO.setTotalScore(teamWithScoreVO.getTotalScore() + score));
                teamWithScoreVO.setAvg(teamWithScoreVO.getTotalScore() * 1.0 / scoreList.size());
            }
            // 6.将list根据总分进行排序
            Collections.sort(list, (t1, t2) -> {
                double diff = t2.getAvg() - t1.getAvg();
                if (diff > 0) return 1;    // t2分数更高，排前面
                if (diff < 0) return -1;   // t1分数更高，排前面
                return 0;                  // 分数相同
            });

            // 7.获取这个比赛的比赛名
            String competitionName = competitionMapper.selectCompetitionNameById(competitionId);

            // 8.对该竞赛取一二三等奖
            int allNum = list.size();
            int first = (int)Math.ceil(allNum * 0.1), second = (int)Math.ceil(allNum * 0.15), third = (int)Math.ceil(allNum * 0.2);
            for (int i = 0; i < first && i < allNum; i++) {
                TeamWithScoreVO teamWithScoreVO = list.get(i);

                // 8.1.获取这个团队的 id
                Integer teamId = teamWithScoreVO.getTeamId();
                // 8.2.获取这个团队队长的学校
                String school = awardMapper.getSchoolByTeamId(teamId, TableConstant.CAPTAIN);
                // 8.3.获取这个团队的成员
                List<String> teamMembers = awardMapper.getTeamMember(teamId);
                String[] teamMembersArray = teamMembers.toArray(new String[0]);
                // 8.4.将这个团队的团队名
                String teamName = teamMapper.selectTeamNameById(teamId);
                // 8.5.生成证书
                CertificateGeneratorProperties properties = new CertificateGeneratorProperties(school, teamName, teamMembersArray, competitionName, ResultConstant.FIRST_PRIZE);
                MultipartFile multipartFile = CertificateGeneratorUtil.create(properties);
                // 8.6.保存证书(压缩&上传)
                MultipartFile multipartFile1 = ThumbFileUtil.startThumbFile(multipartFile);
                String url = minioUtil.upload(multipartFile1);

                Award award = Award.builder()
                        .teamId(teamWithScoreVO.getTeamId())
                        .competitionId(competitionId)
                        .awardUrl(url)
                        .awardRank(TableConstant.FIRST)
                        .avgScore(teamWithScoreVO.getAvg())
                        .build();
                awardService.save(award);
            }
            for (int i = first; i < first + second && i < allNum; i++) {
                TeamWithScoreVO teamWithScoreVO = list.get(i);

                // 8.1.获取这个团队的 id
                Integer teamId = teamWithScoreVO.getTeamId();
                // 8.2.获取这个团队队长的学校
                String school = awardMapper.getSchoolByTeamId(teamId, TableConstant.CAPTAIN);
                // 8.3.获取这个团队的成员
                List<String> teamMembers = awardMapper.getTeamMember(teamId);
                String[] teamMembersArray = teamMembers.toArray(new String[0]);
                // 8.4.将这个团队的团队名
                String teamName = teamMapper.selectTeamNameById(teamId);
                // 8.5.生成证书
                CertificateGeneratorProperties properties = new CertificateGeneratorProperties(school, teamName, teamMembersArray, competitionName, ResultConstant.SECOND_PRIZE);
                MultipartFile multipartFile = CertificateGeneratorUtil.create(properties);
                // 8.6.保存证书(压缩&上传)
                MultipartFile multipartFile1 = ThumbFileUtil.startThumbFile(multipartFile);
                String url = minioUtil.upload(multipartFile1);

                Award award = Award.builder()
                        .teamId(teamWithScoreVO.getTeamId())
                        .competitionId(competitionId)
                        .awardUrl(url)
                        .awardRank(TableConstant.SECOND)
                        .avgScore(teamWithScoreVO.getAvg())
                        .build();
                awardService.save(award);
            }
            for (int i = first + second; i < first + second + third && i < allNum; i++) {
                TeamWithScoreVO teamWithScoreVO = list.get(i);

                // 8.1.获取这个团队的 id
                Integer teamId = teamWithScoreVO.getTeamId();
                // 8.2.获取这个团队队长的学校
                String school = awardMapper.getSchoolByTeamId(teamId, TableConstant.CAPTAIN);
                // 8.3.获取这个团队的成员
                List<String> teamMembers = awardMapper.getTeamMember(teamId);
                String[] teamMembersArray = teamMembers.toArray(new String[0]);
                // 8.4.将这个团队的团队名
                String teamName = teamMapper.selectTeamNameById(teamId);
                // 8.5.生成证书
                CertificateGeneratorProperties properties = new CertificateGeneratorProperties(school, teamName, teamMembersArray, competitionName, ResultConstant.THIRD_PRIZE);
                MultipartFile multipartFile = CertificateGeneratorUtil.create(properties);
                // 8.6.保存证书(压缩&上传)
                MultipartFile multipartFile1 = ThumbFileUtil.startThumbFile(multipartFile);
                String url = minioUtil.upload(multipartFile1);

                Award award = Award.builder()
                        .teamId(teamWithScoreVO.getTeamId())
                        .competitionId(competitionId)
                        .awardUrl(url)
                        .awardRank(TableConstant.THIRD)
                        .avgScore(teamWithScoreVO.getAvg())
                        .build();
                awardService.save(award);
            }
        }

        // 8.更新竞赛的获奖状态
        if (CollectionUtil.isNotEmpty(competitionIds)) {
            competitionService.update(new LambdaUpdateWrapper<Competition>()
                    .set(Competition::getAwardsStatus, TableConstant.AWARDS)
                    .in(Competition::getId, competitionIds)
            );
        }

        log.info("定时处理结束竞赛的颁奖结束, {}", LocalDateTime.now());
    }
}
