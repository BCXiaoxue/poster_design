package com.govy.base.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.govy.model.entity.Judge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.govy.model.vo.CompetitionMyVO;
import com.govy.model.vo.CompetitionWithJudgeVO;
import com.govy.model.vo.ScoreVO;
import com.govy.model.vo.TeamWithWorkVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Govy
* @description 针对表【judge】的数据库操作Mapper
* @createDate 2025-07-04 09:40:39
* @Entity com.govy.model.entity.Judge
*/
public interface JudgeMapper extends BaseMapper<Judge> {

    IPage<CompetitionMyVO> selectPageWithUserId(Page<CompetitionMyVO> page, Integer userId);

    List<TeamWithWorkVO> getCompetitionTeamAndWork(Integer competitionId, Integer userId);

    CompetitionWithJudgeVO getCompetitionJudge(Integer competitionId);

    List<ScoreVO> getCompetitionTeamJudge(Integer competitionId, Integer teamId, Integer userId);

    @Select("select user_id from judge where competition_id = #{competitionId}")
    List<Integer> getJudgeIdByCompetitionId(Integer competitionId);
}




