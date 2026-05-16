package com.govy.base.mapper;

import com.govy.model.entity.Score;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.govy.model.vo.TeamWithScoreVO;

import java.util.List;

/**
* @author Govy
* @description 针对表【score】的数据库操作Mapper
* @createDate 2025-07-04 10:08:22
* @Entity com.govy.model.entity.Score
*/
public interface ScoreMapper extends BaseMapper<Score> {

    List<TeamWithScoreVO> selectTeamScore(Integer competitionId);
}




