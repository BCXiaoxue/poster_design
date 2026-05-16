package com.govy.base.mapper;

import com.govy.model.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.govy.model.vo.NewsVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Govy
* @description 针对表【news】的数据库操作Mapper
* @createDate 2025-07-04 10:05:11
* @Entity com.govy.model.entity.News
*/
public interface NewsMapper extends BaseMapper<News> {

    @Select("select id from news")
    List<Integer> getAllId();

    List<NewsVO> getAllByIds(List<Integer> ids);
}




