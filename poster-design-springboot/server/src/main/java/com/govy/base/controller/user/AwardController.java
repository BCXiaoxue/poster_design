package com.govy.base.controller.user;

import com.govy.base.service.AwardService;
import com.govy.common.result.Result;
import com.govy.model.vo.AwardVO;
import com.govy.model.vo.AwardWorkVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "颁奖管理", description = "颁奖相关接口")
@RestController("awardController")
@RequestMapping("/award")
@RequiredArgsConstructor
@Slf4j
public class AwardController {

    private final AwardService awardService;

    @Operation(summary = "获取每个比赛第一名的队伍作品")
    @GetMapping("/getWinWithCompetition")
    public Result<List<AwardWorkVO>> getWinWithCompetition() {
//        log.info("获取每个比赛第一名的队伍作品");

        List<AwardWorkVO> list = awardService.getWinWithCompetition();

        return Result.success(list);
    }

    @Operation(summary = "获取该用户获奖的所有奖状")
    @GetMapping("/getAwardList")
    public Result<List<String>> getAwardUrlList() {
//        log.info("获取该用户获奖的所有奖状");

        List<String> list = awardService.getAwardUrlList();

        return Result.success(list);
    }
}
