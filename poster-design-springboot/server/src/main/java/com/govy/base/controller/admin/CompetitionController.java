package com.govy.base.controller.admin;

import com.govy.base.service.CompetitionCategoryService;
import com.govy.base.service.CompetitionService;
import com.govy.common.result.Result;
import com.govy.model.dto.*;
import com.govy.model.entity.CompetitionCategory;
import com.govy.model.vo.CompetitionAdminByIdVO;
import com.govy.model.vo.CompetitionAdminLikeVO;
import com.govy.model.vo.CompetitionCategoryDataVO;
import com.govy.model.vo.CompetitionDataVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "竞赛管理", description = "竞赛相关接口")
@RestController("adminCompetitionController")
@RequestMapping("/admin/competition")
@RequiredArgsConstructor
@Slf4j
public class CompetitionController {
    private final CompetitionService competitionService;
    private final CompetitionCategoryService competitionCategoryService;

    /**
     * 搜索竞赛
     * @return
     */
    @Operation(summary = "根据条件搜索竞赛")
    @PostMapping("/getWithLike")
    public Result<List<CompetitionAdminLikeVO>> getWithLike(@RequestBody CompetitionAdminLike competitionAdminLike) {
//        log.info("根据条件搜索竞赛: {}", competitionAdminLike);

        List<CompetitionAdminLikeVO> competitionVOList = competitionService.getWithLike(competitionAdminLike);

        return Result.success(competitionVOList);
    }

    /**
     * 根据竞赛id搜索，竞赛详情
     * @param id
     * @return
     */
    @Operation(summary = "根据竞赛id搜索，竞赛详情")
    @GetMapping("/getByIdWithRedis")
    public Result<CompetitionAdminByIdVO> getByIdWithRedis(@RequestParam Integer id) {
//        log.info("根据竞赛id搜索，竞赛详情: {}", id);

        CompetitionAdminByIdVO competitionAdminByIdVO = competitionService.getByIdWithAll(id);

        return Result.success(competitionAdminByIdVO);
    }


    @Operation(summary = "获取竞赛分类")
    @GetMapping("/getCompetitionCategory")
    public Result<List<CompetitionCategory>> getCompetitionCategory() {
//        log.info("获取竞赛分类");

        List<CompetitionCategory> competitionCategoryList = competitionCategoryService.list();

        return Result.success(competitionCategoryList);
    }


    @Operation(summary = "统计竞赛状态和数量信息")
    @GetMapping("/getCompetitionStatusCount")
    public Result<List<CompetitionDataVO>> getCompetitionStatusCount() {
//        log.info("统计竞赛状态和数量信息");

        List<CompetitionDataVO> competitionDataVOList = competitionService.getCompetitionStatusCount();

        return Result.success(competitionDataVOList);
    }

    @Operation(summary = "统计竞赛分类的数量信息")
    @GetMapping("/getCompetitionCategoryCount")
    public Result<List<CompetitionCategoryDataVO>> getCompetitionCategoryCount() {
//        log.info("统计竞赛分类的数量信息");

        List<CompetitionCategoryDataVO> competitionCategoryDataVO = competitionService.getCompetitionCategoryCount();

        return Result.success(competitionCategoryDataVO);
    }
}
