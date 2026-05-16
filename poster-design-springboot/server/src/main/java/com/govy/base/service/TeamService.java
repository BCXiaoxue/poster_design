package com.govy.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.model.dto.TeamQueryLikeDTO;
import com.govy.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.vo.*;

import java.util.List;

/**
* @author Govy
* @description 针对表【team】的数据库操作Service
* @createDate 2025-07-04 09:53:31
*/
public interface TeamService extends IService<Team> {

    IPage<TeamQueryLikeVO> queryList(TeamQueryLikeDTO queryDTO);

    TeamVO getByIdWithCompetition(Integer teamId);

    void addTeam(Team team);


    void updateTeamTeacherByTeacherId(Integer teacherId, Integer teamId);

    void addMember(Integer teamId, Integer memberId);

    void deleteMember(Integer teamId, Integer memberId);

    void removeByIdWithTeamUser(Integer teamId);

    List<TeamNameVO> getTeamByCaptain();

    IPage<TeamAdminQueryLikeVO> adminQueryList(TeamQueryLikeDTO teamQueryLikeDTO);

    List<CompetitionByTeamIdVO> getTeamCompetition(Integer teamId);

    WorkAndCommentVO getTeamWorkAndComment(Integer teamId, Integer competitionId);

}
