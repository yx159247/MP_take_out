/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.controller;


import io.renren.annotation.Login;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.LoginDTO;
import io.renren.dto.PhoneLoginDto;
import io.renren.service.TokenService;
import io.renren.service.UserService;
import io.renren.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/mp")
@Api(tags="登录接口")
@Slf4j
public class ApiLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private HttpServletRequest request;



    @PostMapping("phoneLogin")
    @ApiOperation("手机号登录")
    public Result<LoginVo> phoneLogin(@RequestBody PhoneLoginDto dto){
        //表单校验
        ValidatorUtils.validateEntity(dto);

        //用户登录
        LoginVo loginVo = userService.phoneLogin(dto);

        return new Result().ok(loginVo);
    }

    @GetMapping("sendValidateCode")
    @ApiOperation("发送验证码")
    public Result sendValidateCode(@RequestParam String phone){
        //表单校验
        ValidatorUtils.validateEntity(phone);
        boolean flag = false;
        //发送短信验证码
        if (StringUtils.isNotEmpty(phone)){
            flag = userService.sendValidateCode(phone);
        }
        if (!flag){
            return new Result().error("获取验证码失败");
        }
        return new Result().ok(null);
    }


    @PostMapping("login")
    @ApiOperation("登录")
    public Result<LoginVo> login(@RequestBody LoginDTO dto){
        //表单校验
        ValidatorUtils.validateEntity(dto);


        //用户登录
        LoginVo loginVo = userService.login(dto);

        return new Result().ok(loginVo);
    }

    @Login
    @PostMapping("wxGetPhone")
    @ApiOperation("微信小程序获取手机号")
    public Result<String> wxGetPhone(@RequestBody LoginDTO dto) {

        String phone = userService.wxGetPhone(dto);

        return new Result().ok(phone);
    }

    @Login
    @PostMapping("logout")
    @ApiOperation("退出")
    public Result logout(@ApiIgnore @RequestAttribute("userId") Long userId){
        tokenService.expireToken(userId);
        return new Result();
    }

}