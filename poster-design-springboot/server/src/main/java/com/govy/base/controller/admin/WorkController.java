package com.govy.base.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.WorkService;
import com.govy.common.result.Result;
import com.govy.model.dto.WorkQueryDTO;
import com.govy.model.vo.UserTeamWorkNewsVO;
import com.govy.model.vo.UserTeamWorkVO;
import com.govy.model.vo.WorkAdminQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "作品和报名管理", description = "作品和报名相关接口")
@RestController("adminWorkController")
@RequestMapping("/admin/work")
@RequiredArgsConstructor
@Slf4j
public class WorkController {
    private final WorkService workService;

    @Operation(summary = "获取作品列表")
    @PostMapping("/list")
    public Result<IPage<WorkAdminQueryVO>> list(@RequestBody WorkQueryDTO workQueryDTO) {
//        log.info("获取作品列表 - 模糊查询: {}", workQueryDTO);
        IPage<WorkAdminQueryVO> list = workService.queryLike(workQueryDTO);
        return Result.success(list);
    }


    @Operation(summary = "用户&队伍&作品的增长趋势统计")
    @GetMapping("/getUserTeamWorkTrend")
    public Result<List<UserTeamWorkVO>> getUserTeamWorkTrend() {
//        log.info("用户&队伍&作品的增长趋势统计");

        List<UserTeamWorkVO> userTeamWorkVOList = workService.getUserTeamWorkTrend();

        return Result.success(userTeamWorkVOList);
    }

    @Operation(summary = "统计一堆数据")
    @GetMapping("/count")
    public Result<UserTeamWorkNewsVO> countAll() {
//        log.info("统计一堆数据");

        UserTeamWorkNewsVO userTeamWorkNewsVO = workService.countAll();

        return Result.success(userTeamWorkNewsVO);
    }
}
