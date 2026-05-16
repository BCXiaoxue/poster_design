package com.govy.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.govy.model.entity.TeamUser;
import com.govy.base.service.TeamUserService;
import com.govy.base.mapper.TeamUserMapper;
import org.springframework.stereotype.Service;

/**
* @author Govy
* @description 针对表【team_user】的数据库操作Service实现
* @createDate 2025-07-07 14:46:59
*/
@Service
public class TeamUserServiceImpl extends ServiceImpl<TeamUserMapper, TeamUser>
    implements TeamUserService{

}




