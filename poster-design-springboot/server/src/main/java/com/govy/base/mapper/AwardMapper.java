package com.govy.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.govy.model.entity.Award;
import com.govy.model.vo.AwardVO;
import com.govy.model.vo.AwardWorkVO;

import java.util.List;

/**
* @author Govy
* @description 针对表【award(颁奖表)】的数据库操作Mapper
* @createDate 2025-07-11 10:16:01
* @Entity com.govy.base.entity.Award
*/
public interface AwardMapper extends BaseMapper<Award> {

    List<AwardVO> getAwardList(Integer competitionId);

    List<AwardWorkVO> getWinWithCompetition(Integer awards);

    String getSchoolByTeamId(Integer teamId, Integer captain);

    List<String> getTeamMember(Integer teamId);

    List<String> getAwardUrlList(Integer userId);
}




