package com.govy.base.mapper;

import com.govy.model.entity.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
* @author Govy
* @description 针对表【image】的数据库操作Mapper
* @createDate 2025-07-05 13:23:47
* @Entity com.govy.model.entity.Image
*/
public interface ImageMapper extends BaseMapper<Image> {

    @Delete("delete from image where image_id = #{id} and category = #{category}")
    void removeByIdAndCategory(Integer id, Integer category);

    void removeByIdsAndCategory(List<Integer> ids, Integer category);
}




