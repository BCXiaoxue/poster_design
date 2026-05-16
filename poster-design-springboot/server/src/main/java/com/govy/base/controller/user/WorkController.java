package com.govy.base.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.TeamService;
import com.govy.base.service.WorkService;
import com.govy.common.result.Result;
import com.govy.model.dto.TeamQueryLikeDTO;
import com.govy.model.dto.WorkEnlistsDTO;
import com.govy.model.entity.Work;
import com.govy.model.vo.TeamQueryLikeVO;
import com.govy.model.vo.TeamVO;
import com.govy.model.vo.WorkVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "作品和报名管理", description = "作品和报名相关接口")
@RestController
@RequestMapping("/work")
@RequiredArgsConstructor
@Slf4j
public class WorkController {
    private final WorkService workService;

    /**
     * 根据id获取作品信息
     * @param workId
     * @return
     */
    @Operation(summary = "根据id获取作品信息")
    @GetMapping("/getById")
    public Result<WorkVO> getByWorkId(@RequestParam Integer workId) {
//        log.info("获取id为{}的作品信息", workId);

        WorkVO workVO = workService.getByIdWithRedis(workId);

        return Result.success(workVO);
    }

    @Operation(summary = "报名和上传作品")
    @PostMapping("/save")
    public Result<String> save(@RequestBody WorkEnlistsDTO workEnlistsDTO) {
//        log.info("报名和上传作品: {}", workEnlistsDTO);

        workService.saveWorkAndEnlist(workEnlistsDTO);

        return Result.success("报名和上传成功");
    }

    /**
     * 查询对于当前用户的队长是否已报名
     * @return
     */
    @Operation(summary = "查询对于当前用户的队长是否已报名")
    @GetMapping("/isEnlisted")
    public Result<Boolean> isEnlisted(@RequestParam Integer competitionId) {
//        log.info("查询对于当前用户的队长是否已报名竞赛{}", competitionId);

        boolean isEnlisted = workService.isEnlisted(competitionId);

        return Result.success(isEnlisted);
    }

}
