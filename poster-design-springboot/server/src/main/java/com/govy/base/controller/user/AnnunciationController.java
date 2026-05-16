package com.govy.base.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.AnnunciationService;
import com.govy.common.result.Result;
import com.govy.model.dto.AnnunciationDTO;
import com.govy.model.dto.AnnunciationQueryLikeDTO;
import com.govy.model.entity.Annunciation;
import com.govy.model.vo.AnnunciationQueryLikeVO;
import com.govy.model.vo.AnnunciationVO;
import com.govy.model.vo.JudgmentId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "大赛通知管理", description = "大赛通知相关接口")
@RestController
@RequestMapping("/annunciation")
@RequiredArgsConstructor
@Slf4j
public class AnnunciationController {
    private final AnnunciationService annunciationService;

    /**
     * 获取通知列表 - 分页模糊查询
     */
    @Operation(summary = "获取通知列表 - 分页模糊查询")
    @PostMapping("/queryList")
    public Result<IPage<AnnunciationQueryLikeVO>> list(@RequestBody AnnunciationQueryLikeDTO queryLikeDTO) {
//        log.info("获取通知列表 - 模糊查询: {}", queryLikeDTO);

        IPage<AnnunciationQueryLikeVO> page = annunciationService.queryLike(queryLikeDTO);

        return Result.success(page);
    }

    @Operation(summary = "根据id查询, 是否有前后id")
    @GetMapping("/judgmentById")
    public Result<JudgmentId> judgmentById(@RequestParam Integer id) {
//        log.info("根据id查询, 是否有前后id: {}", id);
        JudgmentId judgmentId = annunciationService.judgmentById(id);
        return Result.success(judgmentId);
    }

    /**
     * 根据id获取通知详情
     */
    @Operation(summary = "根据id获取通知详情")
    @GetMapping("/getById")
    public Result<AnnunciationVO> getById(@RequestParam Integer id) {
//        log.info("根据id获取通知详情: {}", id);

        AnnunciationVO annunciationVO = annunciationService.getByIdWithRedis(id);

        return Result.success(annunciationVO);
    }


    /**
     * 根据id删除通知
     */
    @Operation(summary = "根据id删除通知")
    @DeleteMapping("/deleteById")
    public Result<String> deleteById(@RequestParam Integer id) {
//        log.info("根据id删除通知: {}", id);

        annunciationService.deleteById(id);

        return Result.success("删除成功");
    }

    /**
     * 根据ids批量删除通知
     * @param ids
     * @return
     */
    @Operation(summary = "根据ids批量删除通知")
    @PostMapping("/deleteByIds")
    public Result<String> deleteByIds(@RequestBody List<Integer> ids) {
//        log.info("批量删除通知: {}", ids);

        annunciationService.deleteByIds(ids);

        return Result.success("删除成功");
    }

    /**
     * 添加通知
     * @param annunciationDTO
     * @return
     */
    @Operation(summary = "添加通知")
    @PostMapping("/add")
    public Result<String> add(@RequestBody AnnunciationDTO annunciationDTO) {
//        log.info("添加通知: {}", annunciationDTO);

        annunciationService.saveAnnunciation(annunciationDTO);

        return Result.success("添加成功");
    }

    /**
     * 根据id修改通知
     * @param annunciationDTO
     * @return
     */
    @Operation(summary = "根据id修改通知", description = "必需提交所有参数")
    @PostMapping("/updateById")
    public Result<String> update(@RequestBody AnnunciationDTO annunciationDTO) {
//        log.info("修改通知: {}", annunciationDTO);

        annunciationService.updateByIdWithRedis(annunciationDTO);

        return Result.success("修改成功");
    }

    // 获取随机的5条通知
    @Operation(summary = "获取随机的number条通知")
    @GetMapping("/getRandom")
    public Result<List<AnnunciationVO>> getRandom(Integer number) {
//        log.info("获取随机的{}条通知",  number);

        List<AnnunciationVO> annunciationVOList = annunciationService.getRandom(number);

        return Result.success(annunciationVOList);
    }
}
