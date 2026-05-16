package com.govy.base.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.govy.base.service.UniversityService;
import com.govy.base.service.UserService;
import com.govy.common.result.Result;
import com.govy.model.dto.UserLoginDTO;
import com.govy.model.dto.UserRegisterDTO;
import com.govy.model.dto.UserUpdateDTO;
import com.govy.model.entity.University;
import com.govy.model.entity.User;
import com.govy.model.vo.UserAllVO;
import com.govy.model.vo.UserVO;
import com.govy.model.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UniversityService universityService;

    @Operation(summary = "用户登录-密码")
    @PostMapping("/login")
    public Result<UserLoginVO> userLogin(@Valid @RequestBody UserLoginDTO userDTO) {
//        log.info("用户登录信息: {}", userDTO);

        UserLoginVO loginVO = userService.userLogin(userDTO);

        return Result.success(loginVO);
    }

    @Operation(summary = "用户登录-手机号验证码")
    @PostMapping("/loginByCode")
    public Result<UserLoginVO> userLoginByCode(@Valid @RequestBody UserLoginDTO userDTO) {
//        log.info("用户登录信息: {}", userDTO);

        UserLoginVO loginVO = userService.userLoginByCode(userDTO);

        return Result.success(loginVO);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<String> userRegister(@Valid @RequestBody UserRegisterDTO userDTO) {
//        log.info("用户注册信息: {}", userDTO);

        userService.userRegister(userDTO);

        return Result.success("注册成功");
    }

    @Operation(summary = "发送验证码")
    @GetMapping("/send-code")
    public Result<String> sendCode(@Valid String phone) {
//        log.info("发送手机号验证码：{}", phone);
        userService.sendCode(phone);
        return Result.success("由于国家政策，个人用户无法调用发送短信的接口，我将Redis中的验证码固定为'123456'");
    }

    @Operation(summary = "模糊查询大学")
    @GetMapping("/university")
    public Result<List<University>> queryUniversity(@RequestParam String name) {
//        log.info("查询大学：{}", name);

        // 如果没有传数据，搜索全部
        if (name.isEmpty()) {
            name = "";
        }

        // 模糊查询
        List<University> list = universityService.list(new LambdaQueryWrapper<University>()
                .like(University::getName, name)
        );

        return Result.success(list);
    }


    @Operation(summary = "根据手机号验证码修改密码")
    @PutMapping("/updateByPhone")
    public Result<String> updateByPhone(@Valid @RequestBody UserUpdateDTO userDTO) {
//        log.info("根据手机号验证码修改密码: {}", userDTO);

        userService.updateByPhone(userDTO);

        return Result.success("修改成功");
    }

    @Operation(summary = "根据原密码修改密码")
    @PutMapping("/updateByPassword")
    public Result<String> updateByPassword(@Valid @RequestBody UserUpdateDTO userDTO) {
//        log.info("根据原密码修改密码: {}", userDTO);

        userService.updateByPassword(userDTO);

        return Result.success("修改成功");
    }

    @Operation(summary = "修改用户其他信息")
    @PutMapping("/updateUser")
    public Result<String> updateUser(@Valid @RequestBody User user) {
//        log.info("修改用户其他信息: {}", user);

        userService.updateUser(user);

        return Result.success("修改成功");
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/getUserInfo")
    public Result<UserVO> getUserInfo() {
//        log.info("获取用户信息");

        UserVO userVO = userService.getByIdWithUniversity();

        return Result.success(userVO);
    }

    @Operation(summary = "根据姓名模糊查询")
    @GetMapping("/queryByName")
    public Result<List<UserVO>> queryByName(@RequestParam String name) {
//        log.info("根据姓名模糊查询：{}", name);

        List<UserVO> list = userService.queryByName(name);

        return Result.success(list);
    }

    @Operation(summary = "根据验证码，更新手机号")
    @PutMapping("/updatePhone")
    public Result<String> updatePhone(@RequestBody UserUpdateDTO userDTO) {
//        log.info("根据验证码，更新手机号: {}", userDTO);

        userService.updatePhone(userDTO);

        return Result.success("修改成功");
    }

    @Operation(summary = "统计用户相关信息")
    @GetMapping("/count")
    public Result<UserAllVO> count() {
//        log.info("统计用户相关信息");

        UserAllVO userAllVO = userService.countUserAll();

        return Result.success(userAllVO);
    }
}
