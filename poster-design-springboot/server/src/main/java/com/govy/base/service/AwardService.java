package com.govy.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.vo.AwardDataVO;
import com.govy.model.entity.Award;
import com.govy.model.vo.AwardWithCompetitionVO;
import com.govy.model.vo.AwardWorkVO;

import java.util.List;

/**
* @author Govy
* @description 针对表【award(颁奖表)】的数据库操作Service
* @createDate 2025-07-11 10:16:01
*/
public interface AwardService extends IService<Award> {

    AwardWithCompetitionVO getAwardList(Integer competitionId);

    List<AwardWorkVO> getWinWithCompetition();

    List<String> getAwardUrlList();

    List<AwardDataVO> getAwardCount();
}
