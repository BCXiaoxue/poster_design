package com.govy.base.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.dto.*;
import com.govy.model.entity.User;
import com.govy.model.vo.UserAllVO;
import com.govy.model.vo.UserCountVO;
import com.govy.model.vo.UserVO;
import com.govy.model.vo.UserLoginVO;

import java.util.List;

/**
* @author Govy
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-05-23 16:34:29
*/
public interface UserService extends IService<User> {

    void userRegister(UserRegisterDTO userDTO);

    UserLoginVO userLogin(UserLoginDTO userDTO);

    void sendCode(String phone);

    void updateByPhone(UserUpdateDTO userDTO);

    void updateByPassword(UserUpdateDTO userDTO);

    void updateUser(User user);

    void updatePhone(UserUpdateDTO userDTO);

    UserLoginVO userLoginByCode(UserLoginDTO userDTO);

    UserVO getByIdWithUniversity();

    UserAllVO countUserAll();

    List<UserVO> queryByName(String name);

    IPage<UserVO> getByPermissionAndName(UserQueryLikeDTO userQueryLikeDTO);

    void updateStatus(UserChangeDTO userChangeDTO);

    UserCountVO UserCountAll();

    List<UserVO> queryByNameAndCompetitionId(String name, Integer competitionId);
}
