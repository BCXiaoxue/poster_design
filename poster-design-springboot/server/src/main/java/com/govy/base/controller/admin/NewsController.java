package com.govy.base.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.NewsService;
import com.govy.common.result.Result;
import com.govy.model.dto.NewsDTO;
import com.govy.model.dto.NewsQueryLikeDTO;
import com.govy.model.vo.JudgmentId;
import com.govy.model.vo.NewsCountVO;
import com.govy.model.vo.NewsQueryLikeVO;
import com.govy.model.vo.NewsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "新闻管理", description = "新闻相关接口")
@RestController("adminNewsController")
@RequestMapping("/admin/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {
    private final NewsService newsService;

    @Operation(summary = "新建新闻")
    @PostMapping("/add")
    public Result<String> add(@RequestBody NewsDTO newsDTO) {
//        log.info("添加新闻: {}", newsDTO);

        newsService.addNews(newsDTO);

        return Result.success("添加成功");
    }

    @Operation(summary = "根据id删除新闻")
    @DeleteMapping("/deleteById")
    public Result<String> deleteById(@RequestParam Integer id) {
//        log.info("删除新闻: {}", id);

        newsService.deleteByIdWithRedis(id);

        return Result.success("删除成功");
    }

    @Operation(summary = "根据id编辑新闻")
    @PutMapping("/updateById")
    public Result<String> updateById(@RequestBody NewsDTO newsDTO) {
//        log.info("编辑新闻: {}", newsDTO);

        newsService.updateByIdWithRedis(newsDTO);

        return Result.success("编辑成功");
    }

    /**
     * 查询获奖列表
     *
     * @param queryDTO
     * @return
     */
    @Operation(summary = "查询获奖列表 - 模糊查询")
    @PostMapping("/queryList")
    public Result<IPage<NewsQueryLikeVO>> queryList(@RequestBody NewsQueryLikeDTO queryDTO) {
//        log.info("查询获奖列表: {}", queryDTO);
        IPage<NewsQueryLikeVO> page = newsService.adminQueryList(queryDTO);
        return Result.success(page);
    }

    @Operation(summary = "统计新闻情况")
    @GetMapping("/countNews")
    public Result<NewsCountVO> countNews() {
//        log.info("统计新闻情况");

        NewsCountVO newsCountVO = newsService.newsCountAll();

        return Result.success(newsCountVO);
    }
}
