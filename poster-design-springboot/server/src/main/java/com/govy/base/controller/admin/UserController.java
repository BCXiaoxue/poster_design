package com.govy.base.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.govy.base.service.UserService;
import com.govy.common.result.Result;
import com.govy.model.dto.UserChangeDTO;
import com.govy.model.dto.UserQueryLikeDTO;
import com.govy.model.vo.UserAllVO;
import com.govy.model.vo.UserCountVO;
import com.govy.model.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController("adminUserController")
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @Operation(summary = "根据用户权限和用户名分页查询")
    @PostMapping("/getByPermissionAndName")
    public Result<IPage<UserVO>> getByPermissionAndName(@RequestBody UserQueryLikeDTO userQueryLikeDTO) {
//        log.info("根据用户权限和用户名分页查询: {}", userQueryLikeDTO);

        IPage<UserVO> list = userService.getByPermissionAndName(userQueryLikeDTO);

        return Result.success(list);
    }

    @Operation(summary = "更改用户状态")
    @PutMapping("/updateStatus")
    public Result<String> updateStatus(@RequestBody UserChangeDTO userChangeDTO) {
//        log.info("更改用户状态: {}", userChangeDTO);

        userService.updateStatus(userChangeDTO);

        return Result.success("更新成功");
    }

    @Operation(summary = "统计普通用户数&管理用户数")
    @GetMapping("/countUserAll")
    public Result<UserCountVO> countUserAll() {
//        log.info("获取普通用户数&管理用户数");

        UserCountVO userCountVO = userService.UserCountAll();

        return Result.success(userCountVO);
    }

    @Operation(summary = "名字模糊查询这个竞赛去掉参赛队员的列表")
    @GetMapping("/queryByNameAndCompetitionId")
    public Result<List<UserVO>> queryByNameAndCompetitionId(@RequestParam(required = false) String name, @RequestParam Integer competitionId) {
//        log.info("名字模糊查询这个竞赛去掉参赛队员的列表: {}, {}", name, competitionId);

        List<UserVO> list = userService.queryByNameAndCompetitionId(name, competitionId);

        return Result.success(list);
    }
}
