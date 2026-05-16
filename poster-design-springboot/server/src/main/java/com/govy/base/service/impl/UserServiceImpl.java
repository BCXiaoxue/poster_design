package com.govy.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.govy.base.mapper.CompetitionMapper;
import com.govy.base.service.UserService;
import com.govy.base.mapper.UserMapper;
import com.govy.common.constant.RedisConstant;
import com.govy.common.constant.TableConstant;
import com.govy.common.handler.BaseException;

import com.govy.common.login.LoginUserHolder;
import com.govy.common.result.ResultCodeEnum;
import com.govy.common.utils.AliSmsUtil;
import com.govy.common.utils.CodeUtil;
import com.govy.common.utils.JwtUtil;
import com.govy.model.dto.*;
import com.govy.model.entity.User;
import com.govy.model.vo.UserAllVO;
import com.govy.model.vo.UserCountVO;
import com.govy.model.vo.UserVO;
import com.govy.model.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author Govy
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-05-23 16:34:29
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private final UserMapper userMapper;
    private final AliSmsUtil aliSmsUtil;
    private final StringRedisTemplate redisTemplate;
    private final CompetitionMapper competitionMapper;

    @Override
    public void userRegister(UserRegisterDTO userDTO) {
        // 1.判断用户的手机号是否存在
        User one = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, userDTO.getPhone())
        );
        // 如果存在,报错--用户已存在
        if (ObjectUtil.isNotNull(one)) {
            throw new BaseException(ResultCodeEnum.USER_ACCOUNT_EXIST);
        }

        // 2.校验验证码
        // 从redis中获取验证码
        String code = redisTemplate.opsForValue().get(RedisConstant.PHONE_CODE + userDTO.getPhone());
        if (StrUtil.isBlank(code)) {
            throw new BaseException(ResultCodeEnum.CODE_NOT_EXIST);
        }
        // 如果错误，则返回错误信息
        if (!code.equals(userDTO.getCode())) {
            throw new BaseException(ResultCodeEnum.CODE_ERROR);
        }

        // 3.使用md5加密
        userDTO.setPassword(DigestUtils.md5Hex(userDTO.getPassword()));

        // 4.保存用户信息
        User user = new User();
        BeanUtil.copyProperties(userDTO, user);
        user.setAuthority(TableConstant.COMMON_USER);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public UserLoginVO userLogin(UserLoginDTO userDTO) {
        // 1.判断手机号是否存在
        User one = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, userDTO.getPhone()));

        if (ObjectUtil.isNull(one)) {
            throw new BaseException(ResultCodeEnum.USER_NOT_EXIST);
        }

        // 2.判断密码是否正确
        if (!one.getPassword().equals(DigestUtils.md5Hex(userDTO.getPassword()))) {
            throw new BaseException(ResultCodeEnum.PASSWORD_ERROR);
        }

        // 3.判断账号是否被禁用
        if (one.getStatus().equals(TableConstant.USER_STATUS_DISABLED)) {
            throw new BaseException(ResultCodeEnum.USER_STATUS_DISABLED);
        }

        // 4.更新登录时间
        this.update(new LambdaUpdateWrapper<User>()
                .set(User::getLastLogin, LocalDateTime.now())
                .eq(User::getId, one.getId())
        );

        // 5.生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", one.getId());
        claims.put("username", one.getUsername());
        String jwt = JwtUtil.createJwt(claims);

        // 6.返回数据
        return new UserLoginVO(jwt, one.getAuthority());
    }

    @Override
    public void sendCode(String phone) {
        // 生成六位验证码
//        String code = CodeUtil.getRandomCode(6);
        String code = "123456";
        // 存入redis
        redisTemplate.opsForValue().set(RedisConstant.PHONE_CODE + phone, code, RedisConstant.APP_LOGIN_CODE_TTL_SEC, TimeUnit.SECONDS);
        // 发送验证码
//        aliSmsUtil.sendCode(phone, code);
//        throw new BaseException(ResultCodeEnum.CODE_RESULT);
    }

    /**
     * 根据手机号验证码修改用户密码
     *
     * @param userDTO
     */
    @Override
    public void updateByPhone(UserUpdateDTO userDTO) {
        // 1.获取验证码
        String code = redisTemplate.opsForValue().get(RedisConstant.PHONE_CODE + userDTO.getPhone());
        if (StrUtil.isBlank(code)) {
            throw new BaseException(ResultCodeEnum.CODE_NOT_EXIST);
        }

        // 2.判断验证码是否正确
        if (!code.equals(userDTO.getCode())) {
            throw new BaseException(ResultCodeEnum.CODE_ERROR);
        }

        // 3.修改密码
        User user = new User();
        BeanUtil.copyProperties(userDTO, user);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updatePasswordByPhone(DigestUtils.md5Hex(userDTO.getNewPassword()), userDTO.getPhone());
    }

    /**
     * 根据原密码修改用户密码
     *
     * @param userDTO
     */
    @Override
    public void updateByPassword(UserUpdateDTO userDTO) {
        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();
        System.out.println(userId);

        // 2.判断原密码是否正确
        User one = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId)
        );

        if (ObjectUtil.isNull(one)) {
            throw new BaseException(ResultCodeEnum.USER_NOT_EXIST);
        }

        if (!one.getPassword().equals(DigestUtils.md5Hex(userDTO.getPassword()))) {
            throw new BaseException(ResultCodeEnum.PASSWORD_ERROR);
        }

        userMapper.updatePasswordById(DigestUtils.md5Hex(userDTO.getNewPassword()), userId);
    }

    @Override
    public void updateUser(User user) {
        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 2.修改用户信息
        user.setUpdateTime(LocalDateTime.now());

        // 3.动态判断参数
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(user.getUsername() != null, User::getUsername, user.getUsername());
        wrapper.set(user.getEmail() != null, User::getEmail, user.getEmail());
//        wrapper.set(user.getPhone() != null, User::getPhone, user.getPhone());
        wrapper.set(user.getGender() != null, User::getGender, user.getGender());
        wrapper.set(user.getBirthday() != null, User::getBirthday, user.getBirthday());
//        wrapper.set(user.getIdCard() != null, User::getIdCard, user.getIdCard());
        wrapper.set(user.getAvatarUrl() != null, User::getAvatarUrl, user.getAvatarUrl());
        wrapper.set(user.getSchoolId() != null, User::getSchoolId, user.getSchoolId());
        wrapper.set(user.getStatus() != null, User::getStatus, user.getStatus());
//        wrapper.set(user.getAuthority() != null, User::getAuthority, user.getAuthority());
        wrapper.set(user.getLastLogin() != null, User::getLastLogin, user.getLastLogin());
//        wrapper.set(user.getPassword() != null, User::getPassword, DigestUtils.md5Hex(user.getPassword()));
//        wrapper.set(user.getCreateTime() != null, User::getCreateTime, user.getCreateTime());
        wrapper.set(user.getUpdateTime() != null, User::getUpdateTime, user.getUpdateTime());
        wrapper.eq(User::getId, userId);

        // 4.完成更新
        userMapper.update(user, wrapper);
    }

    /**
     * 修改手机号
     * @param userDTO
     */
    @Override
    public void updatePhone(UserUpdateDTO userDTO) {
        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();
        User user = this.getById(userId);
        if (ObjectUtil.isNull(user)) {
            throw new BaseException(ResultCodeEnum.USER_NOT_EXIST);
        }
        if (!user.getPhone().equals(userDTO.getPhone())) {
            throw new BaseException(ResultCodeEnum.PHONE_ERROR);
        }

        // 2.获取验证码
        String code = redisTemplate.opsForValue().get(RedisConstant.PHONE_CODE + userDTO.getPhone());

        // 3.判断是否为空
        if (StrUtil.isBlank(code)) {
            throw new BaseException(ResultCodeEnum.CODE_NOT_EXIST);
        }
        // 3.校验验证码
        if (!code.equals(userDTO.getCode())) {
            throw new BaseException(ResultCodeEnum.CODE_ERROR);
        }

        userMapper.updatePhoneById(userDTO.getNewPhone(), userId);
    }

    /**
     * 手机号登录
     * @param userDTO
     * @return
     */
    @Override
    public UserLoginVO userLoginByCode(UserLoginDTO userDTO) {
        // 1.获取用户
        User one = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, userDTO.getPhone())
        );

        // 2.判断用户是否存在, 不存在则抛出异常
        if (ObjectUtil.isNull(one)) {
            throw new BaseException(ResultCodeEnum.USER_NOT_EXIST);
        }

        // 3.判断账号是否被禁用
        if (one.getStatus().equals(TableConstant.USER_STATUS_DISABLED)) {
            throw new BaseException(ResultCodeEnum.USER_STATUS_DISABLED);
        }

        // 4.获取验证码
        String code = redisTemplate.opsForValue().get(RedisConstant.PHONE_CODE + userDTO.getPhone());
        if (StrUtil.isBlank(code)) {
            throw new BaseException(ResultCodeEnum.CODE_NOT_EXIST);
        }

        // 5. 校验验证码， 不一致则抛出异常
        if (!code.equals(userDTO.getCode())) {
            throw new BaseException(ResultCodeEnum.CODE_ERROR);
        }

        // 6.更新登录时间
        this.update(new LambdaUpdateWrapper<User>()
                .set(User::getLastLogin, LocalDateTime.now())
                .eq(User::getId, one.getId())
        );

        // 7.登录成功，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", one.getId());
        claims.put("username", one.getUsername());
        String jwt = JwtUtil.createJwt(claims);
        return new UserLoginVO(jwt, one.getAuthority());
    }

    /**
     * 获取用户信息
     * @return
     */
    @Override
    public UserVO getByIdWithUniversity() {
        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        return userMapper.selectByIdWithUniversity(userId);
    }

    /**
     * 统计用户信息
     * @return
     */
    @Override
    public UserAllVO countUserAll() {
        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 2.统计参与的比赛数
        Integer participateCompetitionNum = userMapper.countParticipateCompetitionNum(userId);

        // 3.统计获奖次数
        Integer winNum = userMapper.countWinNum(userId);

        // 4.统计作品数量
        Integer workNum = userMapper.countWorkNum(userId);

        // 5.统计参与团队数量
        Integer teamNum = userMapper.countTeamNum(userId);

        return new UserAllVO(participateCompetitionNum, winNum, workNum, teamNum);
    }

    /**
     * 根据名称查询用户
     * @param name
     * @return
     */
    @Override
    public List<UserVO> queryByName(String name) {
        return userMapper.queryByName(name, TableConstant.COMMON_USER);
    }

    /**
     * 根据权限和名称查询用户
     * @param userQueryLikeDTO
     * @return
     */
    @Override
    public IPage<UserVO> getByPermissionAndName(UserQueryLikeDTO userQueryLikeDTO) {
        // 1.获取分页参数
        IPage<UserVO> page = new Page<>(userQueryLikeDTO.getPageNo(), userQueryLikeDTO.getPageSize());

        return userMapper.getByPermissionAndName(page, userQueryLikeDTO.getAuthority(),
                userQueryLikeDTO.getUsername(), userQueryLikeDTO.getStatus());
    }

    /**
     * 修改用户状态
     * @param userChangeDTO
     */
    @Override
    public void updateStatus(UserChangeDTO userChangeDTO) {
        this.update(new LambdaUpdateWrapper<User>()
                .set(User::getStatus, userChangeDTO.getStatus())
                .eq(User::getId, userChangeDTO.getId())
        );
    }

    /**
     * 统计用户数量
     * @return
     */
    @Override
    public UserCountVO UserCountAll() {
        // 1.获取普通用户数
        Long userCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getAuthority, TableConstant.COMMON_USER)
        );

        // 2.获取管理员数
        Long adminCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getAuthority, TableConstant.ADMIN)
        );

        return new UserCountVO(userCount, adminCount);
    }

    /**
     * 根据名称和比赛id查询用户
     * @param name
     * @param competitionId
     * @return
     */
    @Override
    public List<UserVO> queryByNameAndCompetitionId(String name, Integer competitionId) {
        // 1.获取用户信息并过滤掉这些ids
        return userMapper.queryByNameAndCompetitionId(name, TableConstant.COMMON_USER, competitionId);
    }


}




