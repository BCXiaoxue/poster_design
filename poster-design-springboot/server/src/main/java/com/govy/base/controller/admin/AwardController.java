package com.govy.base.controller.admin;

import com.govy.base.service.AwardService;
import com.govy.common.result.Result;
import com.govy.model.vo.AwardDataVO;
import com.govy.model.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "颁奖管理", description = "颁奖相关接口")
@RestController("adminAwardController")
@RequestMapping("/admin/award")
@RequiredArgsConstructor
@Slf4j
public class AwardController {

    private final AwardService awardService;

    @Operation(summary = "获取获奖列表")
    @GetMapping("/list")
    public Result<AwardWithCompetitionVO> list(@RequestParam Integer competitionId) {
//        log.info("获取获奖列表 - 获取指定竞赛的获奖列表: {}", competitionId);

        AwardWithCompetitionVO awardWithCompetitionVO = awardService.getAwardList(competitionId);

        return Result.success(awardWithCompetitionVO);
    }

    @Operation(summary = "统计分段段数量信息")
    @GetMapping("/getAwardCount")
    public Result<List<AwardDataVO>> getAwardCount() {
//        log.info("统计分段段数量信息");

        List<AwardDataVO> awardDataVOList = awardService.getAwardCount();

        return Result.success(awardDataVOList);
    }

}
