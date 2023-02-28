package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.common.dto.UserDTO;
import io.renren.common.entity.UserEntity;
import io.renren.common.utils.Result;
import io.renren.dto.UpdateUserInfoDto;
import io.renren.service.UserService;
import io.renren.vo.UserInfoOfSettingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信小程序用户模块
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/1/31
 **/
@RestController
@RequestMapping("mp/user")
@Api(tags = "微信小程序用户模块")
public class ApiUserController {
    @Autowired
    private UserService userService;

    //@Login
    //@PutMapping("updateUserInfo")
    //@ApiOperation("更新用户信息")
    //public Result updateUserInfo(@RequestBody UserDTO userDTO){
    //    boolean flag = userService.updateUserInfo(userDTO);
    //    if (!flag){
    //        return new Result().error("更新用户信息失败");
    //    }
    //    return new Result().ok(null);
    //
    //}
    @Login
    @PutMapping("updateUserInfo")
    @ApiOperation("更新用户信息")
    public Result updateUserInfo(@RequestBody UpdateUserInfoDto updateUserInfoDto){
        boolean flag = userService.updateUserInfo(updateUserInfoDto);
        if (!flag){
            return new Result().error("更新用户信息失败");
        }
        return new Result().ok(null);

    }

    @Login
    @GetMapping("getUserInfo")
    @ApiOperation("获取用户信息")
    public Result getUserInfo(@RequestParam Long userId){
        UserEntity userEntity = userService.selectById(userId);
        UserInfoOfSettingVo userInfoOfSettingVo = new UserInfoOfSettingVo();
        BeanUtils.copyProperties(userEntity,userInfoOfSettingVo);
        return new Result().ok(userInfoOfSettingVo);
    }
}
