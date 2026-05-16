package com.govy.base.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.govy.base.service.ScoreService;
import com.govy.common.constant.RedisConstant;
import com.govy.common.constant.TableConstant;
import com.govy.common.login.LoginUserHolder;
import com.govy.model.dto.CompetitionMyQueryLikeDTO;
import com.govy.model.dto.CompetitionWithJudgeDTO;
import com.govy.model.dto.JudgeDTO;
import com.govy.model.entity.Judge;
import com.govy.base.service.JudgeService;
import com.govy.base.mapper.JudgeMapper;
import com.govy.model.entity.Score;
import com.govy.model.vo.CompetitionMyVO;
import com.govy.model.vo.CompetitionWithJudgeVO;
import com.govy.model.vo.ScoreVO;
import com.govy.model.vo.TeamWithWorkVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Govy
 * @description 针对表【judge】的数据库操作Service实现
 * @createDate 2025-07-04 09:40:39
 */
@Service
@RequiredArgsConstructor
public class JudgeServiceImpl extends ServiceImpl<JudgeMapper, Judge>
        implements JudgeService {

    private final JudgeMapper judgeMapper;
    private final ScoreService scoreService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 获取当前用户的参评比赛列表
     *
     * @return
     */
    @Override
    public IPage<CompetitionMyVO> selectPageList(CompetitionMyQueryLikeDTO queryDTO) {
        // 1.获取当前用户ID
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 2.获取分页参数
        Page<CompetitionMyVO> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());

        // 3.写动态sql,获取列表
        IPage<CompetitionMyVO> competitionMyVOIPage = judgeMapper.selectPageWithUserId(page, userId);

        competitionMyVOIPage.getRecords().forEach(competitionMyVO -> {
            competitionMyVO.setRole(TableConstant.JUDGE_ROLE);
        });

        return competitionMyVOIPage;
    }

    /**
     * 获取当前用户的参评比赛下的团队和作品
     *
     * @param competitionId
     * @return
     */
    @Override
    public List<TeamWithWorkVO> getCompetitionTeamAndWork(Integer competitionId) {
        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 1.获取当前用户的参评比赛下的团队和作品
        return judgeMapper.getCompetitionTeamAndWork(competitionId, userId);

    }

    /**
     * 上传评分和评价
     *
     * @param judgeDTO
     */
    @Override
    public void uploadScoreAndComment(JudgeDTO judgeDTO) {
        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 2.创建 Judge 对象
        Score score = new Score(judgeDTO.getTeamId(), userId, judgeDTO.getScore(), judgeDTO.getComment(), judgeDTO.getCompetitionId());
        scoreService.save(score);
    }

    /**
     * 获取当前比赛下的评委
     * @param competitionId
     * @return
     */
    @Override
    @SneakyThrows
    public CompetitionWithJudgeVO getCompetitionJudge(Integer competitionId) {
        // 1.先从redis中获取数据
        String key = RedisConstant.JUDGE_COMPETITION_PREFIX + competitionId;
        String json = redisTemplate.opsForValue().get(key);
        // 2.如果存在，则直接返回
        if (StrUtil.isNotBlank(json)) {
            return objectMapper.readValue(json, CompetitionWithJudgeVO.class);
        }

        // 3.从数据库中获取
        CompetitionWithJudgeVO competitionJudge = judgeMapper.getCompetitionJudge(competitionId);

        // 4.重建缓存
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(competitionJudge)
                , RedisConstant.REDIS_TIME, TimeUnit.SECONDS);
        return competitionJudge;
    }

    /**
     * 保存比赛下的评委
     * @param competitionWithJudgeDTO
     */
    @Transactional
    @Override
    public void saveCompetitionJudge(CompetitionWithJudgeDTO competitionWithJudgeDTO) {
        // 1.先删除数据
        this.remove(new LambdaQueryWrapper<Judge>()
                .eq(Judge::getCompetitionId, competitionWithJudgeDTO.getCompetitionId())
        );

        // 2.删除redis缓存
        String key = RedisConstant.JUDGE_COMPETITION_PREFIX + competitionWithJudgeDTO.getCompetitionId();
        redisTemplate.delete(key);

        // 3.保存数据
        this.saveBatch(competitionWithJudgeDTO.getJudges().stream().map(judgeId -> new Judge(competitionWithJudgeDTO.getCompetitionId(), judgeId)).toList());
    }

    /**
     * 获取当前比赛的团队的评委评价详情
     *
     * @param competitionId
     * @param teamId
     * @return
     */
    @Override
    public List<ScoreVO> getCompetitionTeamJudge(Integer competitionId, Integer teamId) {
        // 1.获取userId
        Integer userId = LoginUserHolder.getLoginUser().getUserId();
        return judgeMapper.getCompetitionTeamJudge(competitionId, teamId, userId);
    }
}




