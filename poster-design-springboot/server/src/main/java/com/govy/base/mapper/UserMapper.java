package com.govy.base.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.model.entity.User;
import com.govy.model.vo.UserVO;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author Govy
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2025-05-23 16:34:29
* @Entity com.govy.base.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    @Update("update user set password = #{password}, update_time = now() where id = #{userId}")
    void updatePasswordById(String password, Integer userId);

    @Update("update user set password = #{password}, update_time = now() where phone = #{phone}")
    void updatePasswordByPhone(String password, String phone);

    @Update("update user set phone = #{newPhone}, update_time = now() where id = #{userId}")
    void updatePhoneById(String newPhone, Integer userId);

    UserVO selectByIdWithUniversity(Integer userId);

    Integer countParticipateCompetitionNum(Integer userId);

    Integer countWinNum(Integer userId);

    Integer countWorkNum(Integer userId);

    Integer countTeamNum(Integer userId);

    List<UserVO> queryByName(String username, Integer commonUser);

    IPage<UserVO> getByPermissionAndName(IPage<UserVO> page, Integer authority, String username, Integer status);

    List<UserVO> queryByNameAndCompetitionId(String username, Integer commonUser, Integer competitionId);
}




