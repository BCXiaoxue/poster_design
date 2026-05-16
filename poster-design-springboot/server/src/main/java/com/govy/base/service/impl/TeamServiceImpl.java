package com.govy.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.govy.base.service.TeamUserService;
import com.govy.common.constant.TableConstant;
import com.govy.common.handler.BaseException;
import com.govy.common.login.LoginUserHolder;
import com.govy.common.result.ResultCodeEnum;
import com.govy.model.dto.TeamQueryLikeDTO;
import com.govy.model.entity.Team;
import com.govy.base.service.TeamService;
import com.govy.base.mapper.TeamMapper;
import com.govy.model.entity.TeamUser;
import com.govy.model.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Govy
 * @description 针对表【team】的数据库操作Service实现
 * @createDate 2025-07-04 09:53:31
 */
@Service
@RequiredArgsConstructor
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    private final TeamMapper teamMapper;
    private final TeamUserService teamUserService;

    /**
     * 获取队伍列表, 条件&&分页查询
     *
     * @param queryDTO
     * @return
     */
    @Override
    public IPage<TeamQueryLikeVO> queryList(TeamQueryLikeDTO queryDTO) {
        // 1.获取当前用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 2.获取分页参数
        Page<TeamQueryLikeVO> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());

        // 3.写动态sql,获取列表
        return teamMapper.selectPageByUserIdAndTeam(page, queryDTO.getTeam(), userId);
    }

    /**
     * 获取团队信息
     *
     * @param teamId
     * @return
     */
    @Override
    public TeamVO getByIdWithCompetition(Integer teamId) {
        // 1. 根据teamId查询基本团队信息
        TeamVO teamVO = teamMapper.selectCompetitionByTeamId(teamId);
        // 2. 根据teamId查询团队成员信息
        List<TeamUserVO> teamUserList = teamMapper.selectByTeamId(teamId);
        // 3. 将team转为teamVO
        teamVO.setTeamUserList(teamUserList);
        // 4. 返回
        return teamVO;
    }

    /**
     * 新增团队信息
     *
     * @param team
     */
    @Override
    @Transactional
    public void addTeam(Team team) {
        // 1.获取队名是否有重复
        Team one = this.getOne(new LambdaQueryWrapper<Team>().eq(Team::getTeam, team.getTeam()));
        if (one != null) {
            throw new BaseException(ResultCodeEnum.TEAM_NAME_ERROR);
        }

        // 2.插入数据
        teamMapper.insert(team);

        // 3.插入该队成员
        Integer userId = LoginUserHolder.getLoginUser().getUserId();
        TeamUser teamUser = new TeamUser(team.getId(), userId, TableConstant.CAPTAIN);

        // 3.1插入数据
        teamUserService.save(teamUser);
    }

    /**
     * 更新指导老师
     */
    @Override
    public void updateTeamTeacherByTeacherId(Integer teacherId, Integer teamId) {
        // 1.查询该队信息
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BaseException(ResultCodeEnum.NOT_FOUND);
        }

        // 2.更新队信息
        this.update(new LambdaUpdateWrapper<Team>()
                .set(Team::getTeacherId, teacherId)
                .eq(Team::getId, teamId)
        );
    }

    /**
     * 新增队信息
     * @param teamId
     * @param memberId
     */
    @Override
    public void addMember(Integer teamId, Integer memberId) {
        // 1.检查用户是否已加入该队
        TeamUser one = teamUserService.getOne(new LambdaQueryWrapper<TeamUser>()
                .eq(TeamUser::getTeamId, teamId)
                .eq(TeamUser::getUserId, memberId)
        );

        // 2.用户已加入
        if (one != null) {
            throw new BaseException(ResultCodeEnum.ALREADY_EXISTS);
        }

        // 3.保存用户信息
        teamUserService.save(new TeamUser(teamId, memberId, TableConstant.MEMBER));
    }

    /**
     * 删除成员
     * @param teamId
     * @param memberId
     */
    @Override
    public void deleteMember(Integer teamId, Integer memberId) {
        this.teamUserService.remove(new LambdaQueryWrapper<TeamUser>()
                .eq(TeamUser::getTeamId, teamId)
                .eq(TeamUser::getUserId, memberId)
        );
    }

    /**
     * 解散队伍
     * @param teamId
     */
    @Override
    @Transactional
    public void removeByIdWithTeamUser(Integer teamId) {
        // 1.查询该用户是否是队长
        Integer userId = LoginUserHolder.getLoginUser().getUserId();
        TeamUser one = teamUserService.getOne(new LambdaQueryWrapper<TeamUser>()
                .eq(TeamUser::getTeamId, teamId)
                .eq(TeamUser::getUserId, userId)
        );
        if (one == null || !one.getStatus().equals(TableConstant.CAPTAIN)) {
            throw new BaseException(ResultCodeEnum.USER_NOT_CAPTAIN);
        }
        // 2.删除队伍
        this.removeById(teamId);

        // 3.删除队伍成员
        this.teamUserService.remove(new LambdaQueryWrapper<TeamUser>()
                .eq(TeamUser::getTeamId, teamId)
        );
    }

    /**
     * 获取该用户是队长的队伍
     * @return
     */
    @Override
    public List<TeamNameVO> getTeamByCaptain() {
        // 1. 获取当前用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 2. 获取该用户是队长的队伍
        return teamMapper.selectTeamByCaptain(userId, TableConstant.CAPTAIN);
    }

    /**
     * 获取管理员查询列表
     * @param teamQueryLikeDTO
     * @return
     */
    @Override
    public IPage<TeamAdminQueryLikeVO> adminQueryList(TeamQueryLikeDTO teamQueryLikeDTO) {
        // 1.获取分页参数
        Page<TeamAdminQueryLikeVO> page = new Page<>(teamQueryLikeDTO.getPageNo(), teamQueryLikeDTO.getPageSize());

        // 2.写动态sql,获取列表
        return teamMapper.selectPageWithTeamName(page, teamQueryLikeDTO.getTeam());
    }

    /**
     * 获取这个团队参加的比赛
     * @param teamId
     * @return
     */
    @Override
    public List<CompetitionByTeamIdVO> getTeamCompetition(Integer teamId) {
        return teamMapper.getTeamCompetition(teamId);
    }

    /**
     * 获取团队作品和评论
     * @param teamId
     * @param competitionId
     * @return
     */
    @Override
    public WorkAndCommentVO getTeamWorkAndComment(Integer teamId, Integer competitionId) {
        // 1.获取作品列表
        List<WorkAdminVO> workList = teamMapper.getTeamWork(teamId, competitionId);

        // 2.获取评论列表
        List<ScoreVO> commentList = teamMapper.getTeamComment(teamId, competitionId);

        return new WorkAndCommentVO(workList, commentList);
    }


}




