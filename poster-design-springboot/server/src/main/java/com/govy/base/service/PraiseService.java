package com.govy.base.service;

import com.govy.model.dto.PraiseLikedDTO;
import com.govy.model.entity.Praise;
import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.vo.PraiseLikedVO;

/**
* @author Govy
* @description 针对表【praise】的数据库操作Service
* @createDate 2025-07-04 10:12:48
*/
public interface PraiseService extends IService<Praise> {

    PraiseLikedVO workById(Integer workId);

    void addLikeById(PraiseLikedDTO praiseLikedDTO);
}
