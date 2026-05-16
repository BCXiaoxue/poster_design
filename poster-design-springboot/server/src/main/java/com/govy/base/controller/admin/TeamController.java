package com.govy.base.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.TeamService;
import com.govy.common.result.Result;
import com.govy.model.dto.TeamQueryLikeDTO;
import com.govy.model.entity.Team;
import com.govy.model.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "队伍管理", description = "队伍相关接口")
@RestController("adminTeamController")
@RequestMapping("/admin/team")
@RequiredArgsConstructor
@Slf4j
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "获取队伍列表, 根据队名，模糊查询")
    @PostMapping("/queryList")
    public Result<IPage<TeamAdminQueryLikeVO>> queryList(@RequestBody TeamQueryLikeDTO teamQueryLikeDTO) {
//        log.info("查询队伍列表: {}", teamQueryLikeDTO);

        IPage<TeamAdminQueryLikeVO> page = teamService.adminQueryList(teamQueryLikeDTO);

        return Result.success(page);
    }

    @Operation(summary = "获取这个团队所参加的比赛")
    @GetMapping("/getTeamCompetition")
    public Result<List<CompetitionByTeamIdVO>> getTeamCompetition(@RequestParam Integer teamId) {
//        log.info("获取这个团队所参加的比赛: {}", teamId);

        List<CompetitionByTeamIdVO> competitionByTeamIdVOList = teamService.getTeamCompetition(teamId);

        return Result.success(competitionByTeamIdVOList);
    }

    @Operation(summary = "根据这个团队id和竞赛id查询作品和评语")
    @GetMapping("/getTeamWorkAndComment")
    public Result<WorkAndCommentVO> getTeamWorkAndComment(@RequestParam Integer teamId, @RequestParam Integer competitionId) {
//        log.info("根据这个团队id和竞赛id查询作品和评语: {}", teamId);

        WorkAndCommentVO workAndCommentVO = teamService.getTeamWorkAndComment(teamId, competitionId);

        return Result.success(workAndCommentVO);
    }

}
