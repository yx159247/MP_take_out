/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.dao.UserDao;
import io.renren.common.entity.UserEntity;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.entity.TokenEntity;
import io.renren.dto.LoginDTO;
import io.renren.properties.WeixinProperties;
import io.renren.service.TokenService;
import io.renren.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("io.renren.service.UserService")
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<UserDao, UserEntity> implements UserService {
	@Autowired
	private TokenService tokenService;
	@Autowired
	private WeixinProperties weixin;
	@Autowired
	private UserDao userDao;
	@Autowired
	private HttpServletRequest request;

	@Override
	public UserEntity getByMobile(String mobile) {
		return baseDao.getUserByMobile(mobile);
	}

	@Override
	public UserEntity getUserByUserId(Long userId) {
		return baseDao.getUserByUserId(userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> login(LoginDTO dto) {


		System.out.println(dto.getCode());
		String jscode2sessionUrl = weixin.getJscode2sessionUrl() + "?appid=" + weixin.getAppid() + "&secret=" + weixin.getSecret() + "&js_code=" + dto.getCode() + "&grant_type=authorization_code";
		System.out.println(jscode2sessionUrl);
		//String result = httpClientUtil.sendHttpGet(jscode2sessionUrl);
		String result = HttpUtil.get(jscode2sessionUrl);
		System.out.println(result);
		JSONObject jsonObject = JSON.parseObject(result);
		String openid = jsonObject.get("openid").toString();
		System.out.println(openid);

		LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(UserEntity::getOpenid, openid);
		UserEntity user = userDao.selectOne(queryWrapper);
		Map<String, Object> map = new HashMap<>(4);
		if (user == null) {
			UserEntity userEntity = new UserEntity();
			userEntity.setOpenid(openid);
			userEntity.setCreateDate(new Date());
			userDao.insert(userEntity);
			TokenEntity tokenEntity = tokenService.createToken(userEntity.getId());
			map.put("token", tokenEntity.getToken());
			map.put("expire", tokenEntity.getExpireDate().getTime() - System.currentTimeMillis());
			map.put("phone",userEntity.getPhone());
		} else {
			user.setNickName(dto.getNickName());
			user.setAvatarUrl(dto.getAvatarUrl());
			userDao.updateById(user);
			TokenEntity tokenEntity = tokenService.createToken(user.getId());
			map.put("token", tokenEntity.getToken());
			map.put("expire", tokenEntity.getExpireDate().getTime() - System.currentTimeMillis());
			map.put("phone",user.getPhone());
			map.put("userId",user.getId());
		}
		//UserEntity one = userDao.selectOne(queryWrapper);
		//String phone = one.getPhone();

		//获取登录token




		//map.put("phone",phone);

		//
		//UserEntity user = getByMobile(dto.getMobile());
		//AssertUtils.isNull(user, ErrorCode.ACCOUNT_PASSWORD_ERROR);
		//
		////密码错误
		//if(!user.getPassword().equals(DigestUtils.sha256Hex(dto.getPassword()))){
		//	throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
		//}




		return map;
	}

	@Override
	public JSONObject wxGetPhone(LoginDTO dto) {
		String code = dto.getCode();
		System.out.println("code--" + code);
		String token_url = weixin.getGetAccessTokenUrl() + "appid=" + weixin.getAppid() + "&secret=" + weixin.getSecret();
		System.out.println("token_url--" + token_url);
		//String token = httpClientUtil.sendHttpGet(token_url);
		String token = HttpUtil.get(token_url);
		System.out.println("token--" + token);

		String access_token = JSON.parseObject(token).get("access_token").toString();
		System.out.println("access_token--" + access_token);
		String url = weixin.getGetPhoneNumberUrl() + access_token;
		System.out.println("url--" + url);

		JSONObject params = new JSONObject();
		params.put("code", code);
		Map<String, String > heads = new HashMap<>();
		heads.put("Content-Type", "application/json;charset=UTF-8");

		//String result = httpClientUtil.sendHttpPostJson(url, params);
		String result = HttpUtil.post(url, params.toString());

		JSONObject parse = (JSONObject) JSON.parse(result);
		System.out.println(parse);
		Object phone_info = parse.get("phone_info");
		System.out.println(phone_info);
		JSONObject phoneInfo = (JSONObject) JSON.parse(String.valueOf(phone_info));
		String phoneNumber = (String) phoneInfo.get("phoneNumber");
		System.out.println(phoneNumber);

		TokenEntity tokenEntity = tokenService.getByToken(request.getHeader("token"));
		System.out.println("UserId" + tokenEntity.getUserId());
		io.renren.common.entity.UserEntity byId = userDao.selectById(tokenEntity.getUserId());
		log.info(byId.getPhone());
		if(!StringUtils.hasText(byId.getPhone())){
			byId.setPhone(phoneNumber);
			userDao.updateById(byId);
		}
		//if (!byId.getPhone().equals(phoneNumber)){
		//    byId.setPhone(phoneNumber);
		//    userService.updateById(byId);
		//}


		return parse;
	}

}