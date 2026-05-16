package com.govy.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.model.dto.CompetitionMyQueryLikeDTO;
import com.govy.model.dto.CompetitionWithJudgeDTO;
import com.govy.model.dto.JudgeDTO;
import com.govy.model.entity.Judge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.vo.CompetitionMyVO;
import com.govy.model.vo.CompetitionWithJudgeVO;
import com.govy.model.vo.ScoreVO;
import com.govy.model.vo.TeamWithWorkVO;

import java.util.List;

/**
* @author Govy
* @description 针对表【judge】的数据库操作Service
* @createDate 2025-07-04 09:40:39
*/
public interface JudgeService extends IService<Judge> {

    IPage<CompetitionMyVO> selectPageList(CompetitionMyQueryLikeDTO queryDTO);

    List<TeamWithWorkVO> getCompetitionTeamAndWork(Integer competitionId);

    void uploadScoreAndComment(JudgeDTO judgeDTO);

    CompetitionWithJudgeVO getCompetitionJudge(Integer competitionId);

    void saveCompetitionJudge(CompetitionWithJudgeDTO competitionWithJudgeDTO);

    List<ScoreVO> getCompetitionTeamJudge(Integer competitionId, Integer teamId);
}
