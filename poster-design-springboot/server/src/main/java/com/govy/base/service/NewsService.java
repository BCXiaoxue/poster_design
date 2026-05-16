package com.govy.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.model.dto.NewsDTO;
import com.govy.model.dto.NewsQueryLikeDTO;
import com.govy.model.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.vo.JudgmentId;
import com.govy.model.vo.NewsCountVO;
import com.govy.model.vo.NewsQueryLikeVO;
import com.govy.model.vo.NewsVO;

import java.util.List;

/**
* @author Govy
* @description 针对表【news】的数据库操作Service
* @createDate 2025-07-04 10:05:11
*/
public interface NewsService extends IService<News> {

    IPage<NewsQueryLikeVO> queryList(NewsQueryLikeDTO queryDTO);

    NewsVO getByIdWithRedis(Integer id);

    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void updateByIdWithRedis(NewsDTO news);

    void saveNews(NewsVO news);

    JudgmentId judgmentById(Integer id);

    List<NewsVO> getRandom(Integer number);

    void addNews(NewsDTO newsDTO);

    void deleteByIdWithRedis(Integer id);

    IPage<NewsQueryLikeVO> adminQueryList(NewsQueryLikeDTO queryDTO);

    NewsCountVO newsCountAll();
}
