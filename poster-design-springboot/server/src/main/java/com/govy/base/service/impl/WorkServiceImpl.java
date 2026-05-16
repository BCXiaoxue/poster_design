package com.govy.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.govy.base.mapper.*;
import com.govy.base.service.*;
import com.govy.common.constant.RedisConstant;
import com.govy.common.constant.TableConstant;
import com.govy.common.handler.BaseException;
import com.govy.common.login.LoginUserHolder;
import com.govy.common.result.ResultCodeEnum;
import com.govy.model.dto.WorkEnlistsDTO;
import com.govy.model.dto.WorkQueryDTO;
import com.govy.model.entity.*;
import com.govy.model.vo.UserTeamWorkNewsVO;
import com.govy.model.vo.UserTeamWorkVO;
import com.govy.model.vo.WorkAdminQueryVO;
import com.govy.model.vo.WorkVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Govy
 * @description 针对表【work】的数据库操作Service实现
 * @createDate 2025-07-04 10:06:31
 */
@Service
@RequiredArgsConstructor
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work>
        implements WorkService {

    private final StringRedisTemplate redisTemplate;
    private final WorkMapper workMapper;
    private final ObjectMapper objectMapper;
    private final CompetitionTeamService competitionTeamService;
    private final CompetitionMapper competitionMapper;
    private final CompetitionTeamMapper competitionTeamMapper;
    private final UserMapper userMapper;
    private final TeamMapper teamMapper;
    private final UserService userService;
    private final TeamUserService teamService;
    private final NewsService newsService;
    private final AnnunciationService annunciationService;
    private final JudgeMapper judgeMapper;

    /**
     * 作品图片
     * @param workId
     * @return
     */
    @Override
    @SneakyThrows
    public WorkVO getByIdWithRedis(Integer workId) {
        // 1.获取redis缓存key
        String key = RedisConstant.WORK_PREFIX + workId;

        // 2.从redis缓存中获取数据
        String strWorkVO = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(strWorkVO)) {
            // 3.如果存在，则直接返回
            return objectMapper.readValue(strWorkVO, WorkVO.class);
        }

        // 4.如果不存在，则从数据库中查询
        WorkVO workVO = workMapper.getByIdWithTeamName(workId);

        // 5.如果数据库中不存在，则返回404
        if (ObjectUtil.isNull(workVO)) {
            throw new BaseException(ResultCodeEnum.NOT_FOUND);
        }

        // 6.缓存数据
        String jsonWorkVO = objectMapper.writeValueAsString(workVO);
        redisTemplate.opsForValue().set(key, jsonWorkVO, RedisConstant.REDIS_TIME, TimeUnit.SECONDS);

        return workVO;
    }

    /**
     * 保存作品和报名
     * @param workEnlistsDTO
     */
    @Transactional
    @Override
    public void saveWorkAndEnlist(WorkEnlistsDTO workEnlistsDTO) {
        // 1.查询这个比赛的评委人员有哪些
        List<Integer> userIdList = judgeMapper.getJudgeIdByCompetitionId(workEnlistsDTO.getCompetitionId());
        // 1.1查询这个团队的人员
        List<Integer> teamUserIdList = teamMapper.selectUserIdByTeamId(workEnlistsDTO.getTeamId());
        // 1.2判断评委和团队人员是否有冲突
        if (!CollUtil.intersectionDistinct(userIdList, teamUserIdList).isEmpty()) {
            throw new BaseException(ResultCodeEnum.COMPETITION_TEAM_JUDGE_CONFLICT);
        }

        // 2.检查是否超出人员限制
        // 2.1查询这个团队有多少人
        Long teamCount = competitionTeamService.count(new LambdaQueryWrapper<CompetitionTeam>()
                .eq(CompetitionTeam::getTeamId, workEnlistsDTO.getTeamId())
        );
        // 3.查询这个比赛限制多少人
        Long competitionCount = competitionMapper.selectByCompetitionId(workEnlistsDTO.getCompetitionId());

        // 4.比较,如果超出，则抛出异常
        if (teamCount > competitionCount) {
            throw new BaseException(ResultCodeEnum.COMPETITION_TEAM_OVER_LIMIT);
        }

        // 5.检查是否已经报名
        CompetitionTeam one = competitionTeamService.getOne(new LambdaQueryWrapper<CompetitionTeam>()
                .eq(CompetitionTeam::getCompetitionId, workEnlistsDTO.getCompetitionId())
                .eq(CompetitionTeam::getTeamId, workEnlistsDTO.getTeamId())
        );
        // 5.1 如果已经报名，则抛出异常
        if (ObjectUtil.isNotNull(one)) {
            throw new BaseException(ResultCodeEnum.COMPETITION_TEAM_ALREADY_EXISTS);
        }

        // 6.创建比赛
        CompetitionTeam competitionTeam = new CompetitionTeam(workEnlistsDTO.getCompetitionId(), workEnlistsDTO.getTeamId());
        competitionTeamService.save(competitionTeam);

        // 7.创建作品
        List<Work> works = workEnlistsDTO.getWorks().stream().map(
                worksDTO -> Work.builder()
                        .teamId(workEnlistsDTO.getTeamId())
                        .competitionId(workEnlistsDTO.getCompetitionId())
                        .name(worksDTO.getName())
                        .introduction(worksDTO.getIntroduction())
                        .picture(worksDTO.getPicture())
                        .submissionDate(new Date())
                        .build()
        ).toList();

        // 8.保存作品
        this.saveBatch(works);

        // 9.删除这个比赛详情的缓存
        String key = RedisConstant.COMPETITION_PREFIX + workEnlistsDTO.getCompetitionId();
        redisTemplate.delete(key);
    }

    /**
     * 判断是否已经报名
     *
     * @return
     */
    @Override
    public boolean isEnlisted(Integer competitionId) {
        // 1.获取当前用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 2.查询这个用户是否已经报名
        return competitionTeamMapper.selectByUserId(userId, competitionId, TableConstant.CAPTAIN);
    }

    /**
     * 获取作品列表
     *
     * @param workQueryDTO
     * @return
     */
    @Override
    public IPage<WorkAdminQueryVO> queryLike(WorkQueryDTO workQueryDTO) {
        // 1.获取分页参数
        Page<WorkAdminQueryVO> page = new Page<>(workQueryDTO.getPageNo(), workQueryDTO.getPageSize());
        // 2.获取列表
        IPage<WorkAdminQueryVO> list = workMapper.selectWorkByStatusAndName(page ,workQueryDTO.getWorkName(), workQueryDTO.getWorkStatus());
        // 3.返回
        list.getRecords().forEach(work -> work.setWorkStatus(workQueryDTO.getWorkStatus()));
        return list;
    }

    /**
     * 获取用户团队作品动态
     * @return
     */
    @Override
    public List<UserTeamWorkVO> getUserTeamWorkTrend() {
        List<UserTeamWorkVO> list = new ArrayList<>();
        // 1.获取今天的时间
        LocalDate now = LocalDate.now();
        // 2.遍历近7天的最大和最小时间
        for (int i = 0; i < 7; i++) {
            LocalDateTime start = LocalDateTime.of(now, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(now, LocalTime.MAX);
            now = now.minusDays(1);
            // 3.查询这个时间段内注册的用户
            Long userCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .ge(User::getCreateTime, start)
                    .le(User::getCreateTime, end)
            );
            // 4.查询这个时间段内注册的团队
            Long teamCount = teamMapper.selectCount(new LambdaQueryWrapper<Team>()
                    .ge(Team::getCreateTime, start)
                    .le(Team::getCreateTime, end)
            );
            // 5.查询这个时间段内创建的作品
            Long workCount = workMapper.selectCount(new LambdaQueryWrapper<Work>()
                    .ge(Work::getCreateTime, start)
                    .le(Work::getCreateTime, end)
            );
            list.add(new UserTeamWorkVO(Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(start), userCount, teamCount, workCount));
        }
        return CollUtil.reverse(list);
    }

    /**
     * 统计一堆数据
     * @return
     */
    @Override
    public UserTeamWorkNewsVO countAll() {
        // 1.获取用户数
        Long userCount = userService.count();
        // 2.获取团队数
        Long teamCount = teamService.count();
        // 3.获取作品数
        Long workCount = this.count();
        // 4.获取新闻数
        Long newsCount = newsService.count();
        // 5.获取公告数
        Long annunciationCount = annunciationService.count();
        // 6.获取正在进行的比赛数
        Long competitionCount = competitionMapper.selectCount(new LambdaQueryWrapper<Competition>()
                .lt(Competition::getRegistrationEnd, DateUtil.date())
                .ge(Competition::getActivityEnd, DateUtil.date())
        );

        return new UserTeamWorkNewsVO(userCount, teamCount, workCount, newsCount, annunciationCount, competitionCount);
    }
}




