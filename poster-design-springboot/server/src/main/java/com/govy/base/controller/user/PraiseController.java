package com.govy.base.controller.user;

import com.govy.base.service.PraiseService;
import com.govy.common.result.Result;
import com.govy.model.dto.PraiseLikedDTO;
import com.govy.model.vo.PraiseLikedVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "点赞管理", description = "点赞相关接口")
@RestController
@RequestMapping("/praise")
@RequiredArgsConstructor
@Slf4j
public class PraiseController {

    private final PraiseService praiseService;
    /**
     * 查询该用户是否点赞, 以及该作品有多少个赞
     * @param workId
     * @return
     */
    @Operation(summary = "查询该用户是否点赞, 以及该作品有多少个赞")
    @GetMapping("/workByUserId")
    public Result<PraiseLikedVO> workById(@RequestParam Integer workId) {
//        log.info("查询点赞数: {}", workId);

        PraiseLikedVO praiseLikedVO = praiseService.workById(workId);

        return Result.success(praiseLikedVO);
    }

    /**
     * 新增/取消点赞
     * @param praiseLikedDTO
     * @return
     */
    @Operation(summary = "新增/取消点赞")
    @PostMapping("/addLikeById")
    public Result<String> addLikeById(@RequestBody PraiseLikedDTO praiseLikedDTO) {
//        log.info("新增点赞: {}", praiseLikedDTO);

        praiseService.addLikeById(praiseLikedDTO);

        return Result.success("新增/取消点赞成功");
    }
}
