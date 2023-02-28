/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.service;

import io.renren.common.dto.UserDTO;
import io.renren.common.entity.UserEntity;
import io.renren.common.service.BaseService;
import io.renren.dto.UpdateUserInfoDto;
import io.renren.dto.LoginDTO;
import io.renren.dto.PhoneLoginDto;
import io.renren.vo.LoginVo;

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
	LoginVo login(LoginDTO dto);

	/**
	 * 微信小程序获取手机号
	 * @param dto
	 * @return 返回手机号
	 */
	String wxGetPhone(LoginDTO dto);

	/**
	 * 更新用户信息
	 *
	 * @param userDTO 用户dto
	 * @return boolean
	 */
	//boolean updateUserInfo(UserDTO userDTO);

	/**
	 * 手机登录
	 *
	 * @param dto dto
	 * @return {@link LoginVo}
	 */
	LoginVo phoneLogin(PhoneLoginDto dto);

	/**
	 * 发送验证码
	 *
	 * @param phone 电话
	 */
	boolean sendValidateCode(String phone);


	/**
	 * 更新用户电话
	 *
	 * @param updateUserInfoDto 改变电话dto
	 * @return boolean
	 */
	boolean updateUserInfo(UpdateUserInfoDto updateUserInfoDto);
}
