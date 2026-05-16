package com.govy.base.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.govy.model.entity.Competition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.govy.model.vo.CompetitionAdminByIdVO;
import com.govy.model.vo.CompetitionAdminLikeVO;
import com.govy.model.vo.CompetitionMyVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Govy
* @description 针对表【competition】的数据库操作Mapper
* @createDate 2025-07-04 09:47:17
* @Entity com.govy.model.entity.Competition
*/
public interface CompetitionMapper extends BaseMapper<Competition> {

    Page<CompetitionMyVO> selectPageA(Page<Competition> page, String competitionName, Integer userId);

    Page<CompetitionMyVO> selectPageB(Page<Competition> page, String competitionName, Integer userId);

    Page<CompetitionMyVO> selectPageC(Page<Competition> page, String competitionName, Integer userId);

    Page<CompetitionMyVO> selectPageD(Page<Competition> page, String competitionName, Integer userId);

    Page<CompetitionMyVO> selectPageE(Page<Competition> page, String competitionName, Integer userId);

    @Select("SELECT people FROM competition WHERE id = #{competitionId}")
    Long selectByCompetitionId(Integer competitionId);

    List<CompetitionAdminLikeVO> selectPageAdminB(String competitionName, Integer categoryId);

    List<CompetitionAdminLikeVO> selectPageAdminC(String competitionName, Integer categoryId);

    List<CompetitionAdminLikeVO> selectPageAdminD(String competitionName, Integer categoryId);

    List<CompetitionAdminLikeVO> selectPageAdminE(String competitionName, Integer categoryId);



    Page<CompetitionMyVO> selectPageBB(Page<Competition> page, String competitionName, Integer userId);

    Page<CompetitionMyVO> selectPageCC(Page<Competition> page, String competitionName, Integer userId);

    Page<CompetitionMyVO> selectPageDD(Page<Competition> page, String competitionName, Integer userId);

    Page<CompetitionMyVO> selectPageEE(Page<Competition> page, String competitionName, Integer userId);

    CompetitionAdminByIdVO getByIdWithAll(Integer id);

    List<Integer> selectEndCompetition(Integer unawards);

    List<Integer> selectEndCompetitionWithFive(Integer awards);

    @Select("SELECT competition_name FROM competition WHERE id = #{competitionId}")
    String selectCompetitionNameById(Integer competitionId);

    List<Integer> selectUserIdByCompetitionId(Integer competitionId);
}




