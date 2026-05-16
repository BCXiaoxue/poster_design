package com.govy.base.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.govy.model.entity.Work;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.govy.model.vo.WorkAdminQueryVO;
import com.govy.model.vo.WorkUrlVO;
import com.govy.model.vo.WorkVO;

import java.util.List;

/**
* @author Govy
* @description 针对表【work】的数据库操作Mapper
* @createDate 2025-07-04 10:06:31
* @Entity com.govy.model.entity.Work
*/
public interface WorkMapper extends BaseMapper<Work> {

    List<WorkUrlVO> selectWorkImagesByCompetitionId(Integer id);

    WorkVO getByIdWithTeamName(Integer workId);

    IPage<WorkAdminQueryVO> selectWorkByStatusAndName(Page<WorkAdminQueryVO> page, String workName, Integer workStatus);
}




