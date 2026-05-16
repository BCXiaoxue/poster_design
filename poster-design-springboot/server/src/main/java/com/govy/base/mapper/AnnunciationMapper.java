package com.govy.base.mapper;

import com.govy.model.entity.Annunciation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.govy.model.vo.AnnunciationVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Govy
* @description 针对表【annunciation】的数据库操作Mapper
* @createDate 2025-07-04 10:08:12
* @Entity com.govy.model.entity.Annunciation
*/
public interface AnnunciationMapper extends BaseMapper<Annunciation> {

    @Select("select id from annunciation")
    List<Integer> getAllId();

    List<AnnunciationVO> getAllByIds(List<Integer> ids);
}




