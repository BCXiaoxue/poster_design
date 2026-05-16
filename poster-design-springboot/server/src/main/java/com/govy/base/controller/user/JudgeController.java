package com.govy.base.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.JudgeService;
import com.govy.common.result.Result;
import com.govy.model.dto.CompetitionMyQueryLikeDTO;
import com.govy.model.dto.JudgeDTO;
import com.govy.model.vo.CompetitionMyVO;
import com.govy.model.vo.CompetitionWithJudgeVO;
import com.govy.model.vo.ScoreVO;
import com.govy.model.vo.TeamWithWorkVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "评委管理", description = "评委相关接口")
@RestController
@RequestMapping("/judge")
@RequiredArgsConstructor
@Slf4j
public class JudgeController {

    private final JudgeService judgeService;
    /**
     * 分页查询，我的参评比赛
     * @return
     */
    @Operation(summary = "分页查询，我的参评比赛")
    @PostMapping("/queryList")
    public Result<IPage<CompetitionMyVO>> queryList(@RequestBody CompetitionMyQueryLikeDTO queryDTO) {
//        log.info("分页查询，我的参评比赛");

        IPage<CompetitionMyVO> list =judgeService.selectPageList(queryDTO);

        return Result.success(list);
    }

    /**
     * 当前竞赛的团队及作品
     * @param competitionId
     * @return
     */
    @Operation(summary = "当前竞赛的团队及作品")
    @GetMapping("/getCompetitionTeamAndWork")
    public Result<List<TeamWithWorkVO> > getCompetitionTeamAndWork(@RequestParam Integer competitionId) {
//        log.info("当前竞赛的团队及作品");

        List<TeamWithWorkVO> teamWithWorkVOList = judgeService.getCompetitionTeamAndWork(competitionId);

        return Result.success(teamWithWorkVOList);
    }


    @Operation(summary = "上传打分和评语")
    @PostMapping("/uploadScoreAndComment")
    public Result<String> uploadScoreAndComment(@RequestBody JudgeDTO judgeDTO) {
//        log.info("上传打分和评语: {}", judgeDTO);

        judgeService.uploadScoreAndComment(judgeDTO);

        return Result.success("上传成功");
    }

    @Operation(summary = "查询当前用户，对这个团队的竞赛作品的评价详情")
    @GetMapping("/getCompetitionTeamJudge")
    public Result<List<ScoreVO>> getCompetitionTeamJudge(@RequestParam Integer competitionId, @RequestParam Integer teamId) {
//        log.info("查询当前用户，对这个团队的竞赛作品的评价详情");

        List<ScoreVO> scoreVOList = judgeService.getCompetitionTeamJudge(competitionId, teamId);

        return Result.success(scoreVOList);
    }
}
