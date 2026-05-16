package com.govy.base.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.NewsService;
import com.govy.common.result.Result;
import com.govy.model.dto.NewsDTO;
import com.govy.model.dto.NewsQueryLikeDTO;
import com.govy.model.entity.News;
import com.govy.model.vo.AnnunciationVO;
import com.govy.model.vo.JudgmentId;
import com.govy.model.vo.NewsQueryLikeVO;
import com.govy.model.vo.NewsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.service.OpenAPIService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "新闻管理", description = "新闻相关接口")
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {
    private final NewsService newsService;

    /**
     * 查询获奖列表
     * @param queryDTO
     * @return
     */
    @Operation(summary = "查询获奖列表 - 模糊查询")
    @PostMapping("/queryList")
    public Result<IPage<NewsQueryLikeVO>> queryList(@RequestBody NewsQueryLikeDTO queryDTO) {
//        log.info("查询获奖列表: {}", queryDTO);
        IPage<NewsQueryLikeVO> page = newsService.queryList(queryDTO);
        return Result.success(page);
    }

    @Operation(summary = "根据id查询, 是否有前后id")
    @GetMapping("/judgmentById")
    public Result<JudgmentId> judgmentById(@RequestParam Integer id) {
//        log.info("根据id查询, 是否有前后id: {}", id);
        JudgmentId judgmentId = newsService.judgmentById(id);
        return Result.success(judgmentId);
    }

    /**
     * 根据id查询获奖新闻
     * @param id
     * @return
     */
    @Operation(summary = "根据id查询新闻")
    @GetMapping("/queryById")
    public Result<NewsVO> queryById(@RequestParam Integer id) {
//        log.info("查询获奖新闻: {}", id);

        NewsVO newsVO = newsService.getByIdWithRedis(id);

        return Result.success(newsVO);
    }

    /**
     * 删除ById
     * @param id
     * @return
     */
    @Operation(summary = "根据id删除新闻")
    @DeleteMapping("/deleteById")
    public Result<String> deleteById(@RequestParam Integer id) {
//        log.info("删除获奖: {}", id);

        newsService.deleteById(id);

        return Result.success("删除成功");
    }

    /**
     * 删除ByIds
     * @param ids
     * @return
     */
    @Operation(summary = "根据ids批量删除新闻")
    @PostMapping("/deleteByIds")
    public Result<String> deleteByIds(@RequestBody List<Integer> ids) {
//        log.info("删除获奖: {}", ids);

        newsService.deleteByIds(ids);

        return Result.success("删除成功");
    }

    /**
     * 添加新闻
     * @param newsVO
     * @return
     */
    @Operation(summary = "添加新闻")
    @PostMapping("/add")
    public Result<String> add(@RequestBody NewsVO newsVO) {
//        log.info("添加新闻: {}", newsVO);

        newsService.saveNews(newsVO);

        return Result.success("添加成功");
    }

    /**
     * 根据id修改新闻
     * @param newsDTO
     * @return
     */
    @Operation(summary = "根据id修改新闻", description = "必需提交所有参数")
    @PutMapping("/updateById")
    public Result<String> update(@RequestBody NewsDTO newsDTO) {
//        log.info("修改新闻: {}", newsDTO);

        newsService.updateByIdWithRedis(newsDTO);

        return Result.success("修改成功");
    }



    // 获取随机的5条新闻
    @Operation(summary = "获取随机的number条新闻")
    @GetMapping("/getRandom")
    public Result<List<NewsVO>> getRandom(Integer number) {
//        log.info("获取随机的{}条新闻",  number);

        List<NewsVO> newsVOList = newsService.getRandom(number);

        return Result.success(newsVOList);
    }
}
