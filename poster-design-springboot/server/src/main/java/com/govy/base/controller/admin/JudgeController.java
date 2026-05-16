package com.govy.base.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.JudgeService;
import com.govy.common.result.Result;
import com.govy.model.dto.CompetitionMyQueryLikeDTO;
import com.govy.model.dto.CompetitionWithJudgeDTO;
import com.govy.model.dto.JudgeDTO;
import com.govy.model.vo.CompetitionMyVO;
import com.govy.model.vo.CompetitionWithJudgeVO;
import com.govy.model.vo.TeamWithWorkVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "评委管理", description = "评委相关接口")
@RestController("adminJudgeController")
@RequestMapping("/admin/judge")
@RequiredArgsConstructor
@Slf4j
public class JudgeController {

    private final JudgeService judgeService;

    @Operation(summary = "查看这个竞赛下的评委")
    @GetMapping("/getCompetitionJudge")
    public Result<CompetitionWithJudgeVO> getCompetitionJudge(@RequestParam Integer competitionId) {
        CompetitionWithJudgeVO competitionWithJudgeVO = judgeService.getCompetitionJudge(competitionId);
        return Result.success(competitionWithJudgeVO);
    }

    @Operation(summary = "保存这个竞赛下的评委")
    @PostMapping("/saveCompetitionJudge")
    public Result<String> saveCompetitionJudge(@RequestBody CompetitionWithJudgeDTO competitionWithJudgeDTO) {
        judgeService.saveCompetitionJudge(competitionWithJudgeDTO);
        return Result.success("保存成功");
    }
}
