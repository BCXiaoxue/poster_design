package com.govy.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.govy.model.dto.*;
import com.govy.model.entity.Competition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.vo.*;

import java.util.List;

/**
* @author Govy
* @description 针对表【competition】的数据库操作Service
* @createDate 2025-07-04 09:47:17
*/
public interface CompetitionService extends IService<Competition> {

    void deleteById(Integer id);

    void deleteByIds(List<Integer> ids);

    void updateByIdWithRedis(CompetitionDTO competition);

    CompetitionVO getByIdWithRedis(Integer id);

    void saveCompetition(CompetitionDTO competition);

    Page<CompetitionMyVO> getByIdWithLike(CompetitionMyQueryLikeDTO competitionMyQueryLikeDTO);

    List<CompetitionAdminLikeVO> getWithLike(CompetitionAdminLike competitionAdminLike);

    CompetitionAdminByIdVO getByIdWithAll(Integer id);

    Page<CompetitionMyVO> getByNameWithStatus(CompetitionMyQueryLikeDTO competitionMyQueryLikeDTO);

    List<CompetitionDataVO> getCompetitionStatusCount();

    List<CompetitionCategoryDataVO> getCompetitionCategoryCount();
}
