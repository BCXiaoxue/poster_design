package com.govy.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.govy.common.constant.RedisConstant;
import com.govy.common.login.LoginUserHolder;
import com.govy.model.dto.PraiseLikedDTO;
import com.govy.model.entity.Praise;
import com.govy.base.service.PraiseService;
import com.govy.base.mapper.PraiseMapper;
import com.govy.model.vo.PraiseLikedVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
* @author Govy
* @description 针对表【praise】的数据库操作Service实现
* @createDate 2025-07-04 10:12:48
*/
@Service
@RequiredArgsConstructor
public class PraiseServiceImpl extends ServiceImpl<PraiseMapper, Praise>
    implements PraiseService{

    private final StringRedisTemplate redisTemplate;

    /**
     * 获取点赞信息
     * @param workId
     * @return
     */
    @Override
    public PraiseLikedVO workById(Integer workId) {
        // 1.获取当前用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 2.查询当前用户是否点赞
        Double score = redisTemplate.opsForZSet().score(RedisConstant.WORK_LIKE_PREFIX + workId, String.valueOf(userId));
        Integer liked = score == null ? 0 : 1;

        // 3.查询当前作品点赞数量
        Long likeCount = redisTemplate.opsForZSet().size(RedisConstant.WORK_LIKE_PREFIX + workId);

        return new PraiseLikedVO(liked, likeCount);
    }

    /**
     * 新增/取消点赞
     * @param praiseLikedDTO
     */
    @Override
    public void addLikeById(PraiseLikedDTO praiseLikedDTO) {
        // 1.获取当前用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 2.判断当前用户是否点赞
        Double score = redisTemplate.opsForZSet().score(RedisConstant.WORK_LIKE_PREFIX + praiseLikedDTO.getWorkId(), String.valueOf(userId));
        if (score != null) {
            // 2.1.已点赞
            redisTemplate.opsForZSet().remove(RedisConstant.WORK_LIKE_PREFIX + praiseLikedDTO.getWorkId(), String.valueOf(userId));
        } else {
            // 2.2.未点赞
            redisTemplate.opsForZSet().add(RedisConstant.WORK_LIKE_PREFIX + praiseLikedDTO.getWorkId(), String.valueOf(userId), System.currentTimeMillis());
        }
    }
}




