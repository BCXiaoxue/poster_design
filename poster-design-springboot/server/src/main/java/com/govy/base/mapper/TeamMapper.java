package com.govy.base.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.govy.model.entity.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.govy.model.vo.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Govy
* @description 针对表【team】的数据库操作Mapper
* @createDate 2025-07-04 09:53:31
* @Entity com.govy.model.entity.Team
*/
public interface TeamMapper extends BaseMapper<Team> {

    IPage<TeamQueryLikeVO> selectPageByUserIdAndTeam(Page<TeamQueryLikeVO> page, String team, Integer userId);

    List<TeamUserVO> selectByTeamId(Integer teamId);

    TeamVO selectCompetitionByTeamId(Integer teamId);

    List<TeamNameVO> selectTeamByCaptain(Integer userId, Integer captain);

    @Select("select team_status from team where id = #{teamId}")
    Integer selectTeamStatusByTeamId(Integer teamId);

    List<Integer> selectUserIdByTeamId(Integer teamId);

    IPage<TeamAdminQueryLikeVO> selectPageWithTeamName(Page<TeamAdminQueryLikeVO> page, String teamName);

    List<CompetitionByTeamIdVO> getTeamCompetition(Integer teamId);

    List<WorkAdminVO> getTeamWork(Integer teamId, Integer competitionId);

    List<ScoreVO> getTeamComment(Integer teamId, Integer competitionId);

    @Select("select team from team where id = #{teamId}")
    String selectTeamNameById(Integer teamId);
}




