package com.govy.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.model.dto.WorkEnlistsDTO;
import com.govy.model.dto.WorkQueryDTO;
import com.govy.model.entity.Work;
import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.vo.UserTeamWorkNewsVO;
import com.govy.model.vo.UserTeamWorkVO;
import com.govy.model.vo.WorkAdminQueryVO;
import com.govy.model.vo.WorkVO;

import java.util.List;

/**
* @author Govy
* @description 针对表【work】的数据库操作Service
* @createDate 2025-07-04 10:06:31
*/
public interface WorkService extends IService<Work> {

    WorkVO getByIdWithRedis(Integer workId);

    void saveWorkAndEnlist(WorkEnlistsDTO workEnlistsDTO);

    boolean isEnlisted(Integer competitionId);

    IPage<WorkAdminQueryVO> queryLike(WorkQueryDTO workQueryDTO);

    List<UserTeamWorkVO> getUserTeamWorkTrend();

    UserTeamWorkNewsVO countAll();
}
