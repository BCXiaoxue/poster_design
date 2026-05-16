package com.govy.base.mapper;

import com.govy.model.vo.CompetitionCategoryDataVO;
import com.govy.model.entity.CompetitionCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Govy
* @description 针对表【competition_category】的数据库操作Mapper
* @createDate 2025-07-05 15:50:48
* @Entity com.govy.model.entity.CompetitionCategory
*/
public interface CompetitionCategoryMapper extends BaseMapper<CompetitionCategory> {

    List<CompetitionCategoryDataVO> getCompetitionCategoryCount();
}




