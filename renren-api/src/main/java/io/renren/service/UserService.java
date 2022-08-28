/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.service;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.entity.UserEntity;
import io.renren.common.service.BaseService;
import io.renren.dto.LoginDTO;

import java.util.Map;

/**
 * 用户
 * 
 * @author Mark sunlightcs@gmail.com
 */
public interface UserService extends BaseService<UserEntity> {

	UserEntity getByMobile(String mobile);

	UserEntity getUserByUserId(Long userId);

	/**
	 * 用户登录
	 * @param dto    登录表单
	 * @return        返回登录信息
	 */
	Map<String, Object> login(LoginDTO dto);

	/**
	 * 微信小程序获取手机号
	 * @param dto
	 * @return 返回手机号
	 */
	JSONObject wxGetPhone(LoginDTO dto);
}
