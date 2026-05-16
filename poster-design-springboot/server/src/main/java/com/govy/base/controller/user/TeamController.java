package com.govy.base.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.TeamService;
import com.govy.common.result.Result;
import com.govy.model.dto.TeamQueryLikeDTO;
import com.govy.model.entity.Team;
import com.govy.model.vo.TeamNameVO;
import com.govy.model.vo.TeamQueryLikeVO;
import com.govy.model.vo.TeamVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "队伍管理", description = "队伍相关接口")
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    private final TeamService teamService;
    /**
     * 根据userId和条件分页查询，队伍列表
     */
    @Operation(summary = "根据userId和条件查询，分页队伍列表")
    @PostMapping("/queryList")
    public Result<IPage<TeamQueryLikeVO>> queryList(@RequestBody TeamQueryLikeDTO queryDTO) {
//        log.info("查询队伍列表: {}", queryDTO);

        IPage<TeamQueryLikeVO> page = teamService.queryList(queryDTO);

        return Result.success(page);
    }

    @Operation(summary = "根据teamId查询队伍信息")
    @GetMapping("/getById")
    public Result<TeamVO> getById(@RequestParam Integer teamId) {
//        log.info("查询队伍信息: {}", teamId);

        TeamVO teamVO = teamService.getByIdWithCompetition(teamId);

        return Result.success(teamVO);
    }

    @Operation(summary = "添加队伍")
    @PostMapping("/add")
    public Result<String> add(@RequestBody Team team) {
//        log.info("添加队伍: {}", team);

        teamService.addTeam(team);

        return Result.success("添加成功");
    }

    @Operation(summary = "更新指导老师")
    @PutMapping("/updateTeacher")
    public Result<String> updateTeacher(Integer teacherId, Integer teamId) {
//        log.info("更新指导老师: {}", teacherId);

        teamService.updateTeamTeacherByTeacherId(teacherId, teamId);

        return Result.success("更新成功");
    }

    @Operation(summary = "新增队员")
    @PostMapping("/addMember")
    public Result<String> addMember(Integer teamId, Integer memberId) {
//        log.info("新增队员: {}", memberId);

        teamService.addMember(teamId, memberId);

        return Result.success("新增队员成功");
    }

    @Operation(summary = "删除队员")
    @DeleteMapping("/deleteMember")
    public Result<String> deleteMember(@RequestParam Integer teamId, @RequestParam Integer memberId) {
//        log.info("删除队员: {}", memberId);

        teamService.deleteMember(teamId, memberId);

        return Result.success("删除队员成功");
    }


    @Operation(summary = "解散该团队")
    @DeleteMapping("/dissolveTeam")
    public Result<String> dissolveTeam(@RequestParam Integer teamId) {
//        log.info("解散团队: {}", teamId);

        teamService.removeByIdWithTeamUser(teamId);

        return Result.success("解散团队成功");
    }


    @Operation(summary = "获取该用户下，作为队长的团队名和id列表")
    @GetMapping("/getTeamByCaptain")
    public Result<List<TeamNameVO>> getTeamByCaptain() {
//        log.info("获取该用户下，作为队长的团队");

        List<TeamNameVO> list = teamService.getTeamByCaptain();

        return Result.success(list);
    }
}
