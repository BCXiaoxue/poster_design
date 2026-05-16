package com.govy.base.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.govy.base.service.CompetitionService;
import com.govy.common.result.Result;
import com.govy.model.dto.CompetitionDTO;
import com.govy.model.dto.CompetitionMyQueryLikeDTO;
import com.govy.model.entity.Competition;
import com.govy.model.vo.CompetitionMyVO;
import com.govy.model.vo.CompetitionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "竞赛管理", description = "竞赛相关接口")
@RestController
@RequestMapping("/competition")
@RequiredArgsConstructor
@Slf4j
public class CompetitionController {
    private final CompetitionService competitionService;

    /**
     * 新增竞赛信息
     * @param competitionDTO
     * @return
     */
    @Operation(summary = "新增竞赛信息")
    @PostMapping("/add")
    public Result<String> add(@RequestBody CompetitionDTO competitionDTO) {
//        log.info("添加竞赛: {}", competitionDTO);

        competitionService.saveCompetition(competitionDTO);

        return Result.success("添加成功");
    }

    /**
     * 根据id删除竞赛信息
     * @param id
     * @return
     */
    @Operation(summary = "根据id删除竞赛信息")
    @DeleteMapping("/deleteById")
    public Result<String> deleteById(@RequestParam Integer id) {
//        log.info("删除竞赛: {}", id);

        competitionService.deleteById(id);

        return Result.success("删除成功");
    }

    /**
     * 批量删除竞赛信息
     * @param ids
     * @return
     */
    @Operation(summary = "批量删除竞赛信息")
    @PostMapping("/deleteByIds")
    public Result<String> deleteByIds(@RequestBody List<Integer> ids) {
//        log.info("批量删除竞赛: {}", ids);

        competitionService.deleteByIds(ids);

        return Result.success("删除成功");
    }

    /**
     * 根据id修改竞赛信息
     * @param competitionDTO
     * @return
     */
    @PutMapping("/updateById")
    @Operation(summary = "根据id修改竞赛信息")
    public Result<String> updateById(@RequestBody CompetitionDTO competitionDTO) {
//        log.info("更新竞赛: {}", competitionDTO);

        competitionService.updateByIdWithRedis(competitionDTO);

        return Result.success("更新成功");
    }

    /**
     * 根据id查询竞赛信息
     * @param id
     * @return
     */
    @GetMapping("/getById")
    @Operation(summary = "根据id获取竞赛信息")
    public Result<CompetitionVO> getById(@RequestParam Integer id) {
//        log.info("获取id为{}的竞赛信息", id);

        CompetitionVO competitionVO = competitionService.getByIdWithRedis(id);

        return Result.success(competitionVO);
    }

    /**
     * 根据竞赛名和竞赛状态分页查询获取竞赛信息
     */
    @PostMapping("/getByNameAndStatus")
    @Operation(summary = "根据竞赛名和竞赛状态分页查询获取参与的竞赛信息(status未开始-0, 招募中-1，进行中-2，已结束-3)")
    public Result<Page<CompetitionMyVO>> getByIdWithLike(@RequestBody CompetitionMyQueryLikeDTO competitionMyQueryLikeDTO) {
//        log.info("根据竞赛名和竞赛状态分页查询获取竞赛信息：{}", competitionMyQueryLikeDTO);

        Page<CompetitionMyVO> competitionMyVOList = competitionService.getByIdWithLike(competitionMyQueryLikeDTO);

        return Result.success(competitionMyVOList);
    }

    /**
     * 获取所有竞赛信息
     * @return
     */
    @PostMapping("/getByNameWithStatus")
    @Operation(summary = "根据竞赛名和竞赛状态分页查询获取竞赛信息(status未开始-0, 招募中-1，进行中-2，已结束-3)")
    public Result<Page<CompetitionMyVO>> getByNameWithStatus(@RequestBody CompetitionMyQueryLikeDTO competitionMyQueryLikeDTO) {
//        log.info("根据竞赛名和竞赛状态分页查询获取竞赛信息：{}", competitionMyQueryLikeDTO);

        Page<CompetitionMyVO> competitionMyVOList = competitionService.getByNameWithStatus(competitionMyQueryLikeDTO);

        return Result.success(competitionMyVOList);
    }
}
