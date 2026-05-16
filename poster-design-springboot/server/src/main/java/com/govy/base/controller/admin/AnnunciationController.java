package com.govy.base.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.AnnunciationService;
import com.govy.common.result.Result;
import com.govy.model.dto.AnnunciationDTO;
import com.govy.model.dto.AnnunciationQueryLikeDTO;
import com.govy.model.vo.AnnunciationCountVO;
import com.govy.model.vo.AnnunciationQueryLikeVO;
import com.govy.model.vo.AnnunciationVO;
import com.govy.model.vo.JudgmentId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "大赛通知管理", description = "大赛通知相关接口")
@RestController("adminAnnunciationController")
@RequestMapping("/admin/annunciation")
@RequiredArgsConstructor
@Slf4j
public class AnnunciationController {
    private final AnnunciationService annunciationService;

    /**
     * 添加通知
     * @param annunciation
     * @return
     */
    @Operation(summary = "添加通知")
    @PostMapping("/add")
    public Result<String> add(@RequestBody AnnunciationDTO annunciation) {
//        log.info("添加通知: {}", annunciation);

        annunciationService.addAnnunciation(annunciation);

        return Result.success("添加成功");
    }

    /**
     * 删除通知
     * @param id
     * @return
     */
    @Operation(summary = "根据id删除通知")
    @DeleteMapping("/deleteById")
    public Result<String> deleteById(@RequestParam Integer id) {
//        log.info("删除通知: {}", id);

        annunciationService.deleteByIdWithRedis(id);

        return Result.success("删除成功");
    }

    /**
     * 修改通知
     * @param annunciationDTO
     * @return
     */
    @Operation(summary = "根据id编辑通知")
    @PutMapping("/updateById")
    public Result<String> updateById(@RequestBody AnnunciationDTO annunciationDTO) {
//        log.info("修改通知: {}", annunciationDTO);

        annunciationService.updateByIdWithRedis(annunciationDTO);

        return Result.success("修改成功");
    }

    /**
     * 获取通知列表 - 分页模糊查询
     */
    @Operation(summary = "获取通知列表 - 分页模糊查询")
    @PostMapping("/queryList")
    public Result<IPage<AnnunciationQueryLikeVO>> list(@RequestBody AnnunciationQueryLikeDTO queryLikeDTO) {
//        log.info("获取通知列表 - 模糊查询: {}", queryLikeDTO);

        IPage<AnnunciationQueryLikeVO> page = annunciationService.adminQueryLike(queryLikeDTO);

        return Result.success(page);
    }


    @Operation(summary = "统计大赛通知数")
    @GetMapping("/countAnnunciation")
    public Result<AnnunciationCountVO> countAnnunciation() {
//        log.info("统计大赛通知数");

        AnnunciationCountVO annunciationCountVO = annunciationService.annunciationCountAll();

        return Result.success(annunciationCountVO);
    }
}
