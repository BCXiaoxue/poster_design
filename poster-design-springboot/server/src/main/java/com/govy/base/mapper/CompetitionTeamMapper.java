package com.govy.base.mapper;

import com.govy.model.entity.CompetitionTeam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Govy
* @description 针对表【competition_team】的数据库操作Mapper
* @createDate 2025-07-07 16:19:28
* @Entity com.govy.model.entity.CompetitionTeam
*/
public interface CompetitionTeamMapper extends BaseMapper<CompetitionTeam> {

    Boolean selectByUserId(Integer userId, Integer competitionId ,Integer captain);
}




