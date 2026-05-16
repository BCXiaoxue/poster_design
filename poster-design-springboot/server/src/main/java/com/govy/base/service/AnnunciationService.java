package com.govy.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.model.dto.AnnunciationDTO;
import com.govy.model.dto.AnnunciationQueryLikeDTO;
import com.govy.model.entity.Annunciation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.vo.AnnunciationCountVO;
import com.govy.model.vo.AnnunciationQueryLikeVO;
import com.govy.model.vo.AnnunciationVO;
import com.govy.model.vo.JudgmentId;

import java.util.List;

/**
* @author Govy
* @description 针对表【annunciation】的数据库操作Service
* @createDate 2025-07-04 10:08:12
*/
public interface AnnunciationService extends IService<Annunciation> {

    IPage<AnnunciationQueryLikeVO> queryLike(AnnunciationQueryLikeDTO queryLikeDTO);

    AnnunciationVO getByIdWithRedis(Integer id);

    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void updateByIdWithRedis(AnnunciationDTO annunciation);

    void saveAnnunciation(AnnunciationDTO annunciation);

    JudgmentId judgmentById(Integer id);

    List<AnnunciationVO> getRandom(Integer number);

    void addAnnunciation(AnnunciationDTO annunciationDTO);

    void deleteByIdWithRedis(Integer id);

    IPage<AnnunciationQueryLikeVO> adminQueryLike(AnnunciationQueryLikeDTO queryLikeDTO);

    AnnunciationCountVO annunciationCountAll();
}
