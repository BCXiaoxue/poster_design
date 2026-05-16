package com.govy.base.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.govy.base.mapper.CompetitionMapper;
import com.govy.base.service.AwardService;
import com.govy.base.mapper.AwardMapper;
import com.govy.common.constant.RedisConstant;
import com.govy.common.constant.TableConstant;
import com.govy.common.login.LoginUserHolder;
import com.govy.model.vo.*;
import com.govy.model.entity.Award;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Govy
 * @description 针对表【award(颁奖表)】的数据库操作Service实现
 * @createDate 2025-07-11 10:16:01
 */
@Service
@RequiredArgsConstructor
public class AwardServiceImpl extends ServiceImpl<AwardMapper, Award>
        implements AwardService {

    private final AwardMapper awardMapper;
    private final CompetitionMapper competitionMapper;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 获取获奖列表
     *
     * @param competitionId
     * @return
     */
    @Override
    @SneakyThrows
    public AwardWithCompetitionVO getAwardList(Integer competitionId) {
        // 1.先从数据库中查询获奖列表
        String key = RedisConstant.AWARD_COMPETITION_PREFIX + competitionId;
        String json = redisTemplate.opsForValue().get(key + competitionId);
        if (StrUtil.isNotBlank(json)) {
            return objectMapper.readValue(json, AwardWithCompetitionVO.class);
        }

        // 2. 获取获奖列表
        List<AwardVO> awardList = awardMapper.getAwardList(competitionId);

        // 3.获取竞赛名
        String competitionName = competitionMapper.selectCompetitionNameById(competitionId);

        // 4.封装VO数据
        AwardWithCompetitionVO awardWithCompetitionVO = AwardWithCompetitionVO.builder()
                .competitionName(competitionName)
                .awardList(awardList)
                .build();

        // 5.重建缓存
        redisTemplate.opsForValue().set(key,
                objectMapper.writeValueAsString(awardWithCompetitionVO),
                RedisConstant.REDIS_TIME,
                TimeUnit.SECONDS
        );

        return awardWithCompetitionVO;
    }

    @Override
    @SneakyThrows
    public List<AwardWorkVO> getWinWithCompetition() {
        // 1.查看缓存是否存在top5
        String s = redisTemplate.opsForValue().get(RedisConstant.AWARD_WIN_WITH_COMPETITION);
        if (StrUtil.isNotBlank(s)) {
            return objectMapper.readValue(s, AwardWorkTop5.class).getAwardWorkVOList();
        }

        // 2.获取近5期结束的竞赛的获奖列表
        List<AwardWorkVO> winWithCompetition = awardMapper.getWinWithCompetition(TableConstant.AWARDS);

        // 3.重建缓存
        AwardWorkTop5 winWithCompetitionTop5 = AwardWorkTop5.builder()
                .awardWorkVOList(winWithCompetition)
                .build();
        String json = objectMapper.writeValueAsString(winWithCompetitionTop5);
        redisTemplate.opsForValue().set(RedisConstant.AWARD_WIN_WITH_COMPETITION, json);

        return winWithCompetition;
    }

    /**
     * 获取获奖url列表
     *
     * @return
     */
    @Override
    public List<String> getAwardUrlList() {
        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();
        // 2.获取获奖url列表
        return awardMapper.getAwardUrlList(userId);
    }

    /**
     * 统计分段段数量信息
     *
     * @return
     */
    @Override
    public List<AwardDataVO> getAwardCount() {
        // 1.获取平均分90及以上的团队人数
        Long countA = awardMapper.selectCount(new LambdaQueryWrapper<Award>()
                .ge(Award::getAvgScore, 90)
        );

        // 2.获取平均分80-90之间的团队人数
        Long countB = awardMapper.selectCount(new LambdaQueryWrapper<Award>()
                .ge(Award::getAvgScore, 80)
                .lt(Award::getAvgScore, 90)
        );

        // 3.获取平均分70-80之间的团队人数
        Long countC = awardMapper.selectCount(new LambdaQueryWrapper<Award>()
                .ge(Award::getAvgScore, 70)
                .lt(Award::getAvgScore, 80)
        );

        // 4.获取平均分60-70之间的团队人数
        Long countD = awardMapper.selectCount(new LambdaQueryWrapper<Award>()
                .ge(Award::getAvgScore, 60)
                .lt(Award::getAvgScore, 70)
        );

        // 5.获取平均分60以下的团队人数
        Long countE = awardMapper.selectCount(new LambdaQueryWrapper<Award>()
                .lt(Award::getAvgScore, 60)
        );

        return List.of(
                new AwardDataVO("90分及以上", countA, "#67c23a"),
                new AwardDataVO("80~90分", countB, "#409eff"),
                new AwardDataVO("70~80分", countC, "#e6a23c"),
                new AwardDataVO("60~70分", countD, "#f56c6c"),
                new AwardDataVO("60分以下", countE, "#909399")
        );
    }

}




