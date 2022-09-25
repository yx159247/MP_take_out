/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.dao.UserDao;
import io.renren.common.entity.UserEntity;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.entity.TokenEntity;
import io.renren.dto.LoginDTO;
import io.renren.properties.WeixinProperties;
import io.renren.service.TokenService;
import io.renren.service.UserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
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
	@Autowired
	private WxMaService wxMaService;

	@Override
	public UserEntity getByMobile(String mobile) {
		return baseDao.getUserByMobile(mobile);
	}

	@Override
	public UserEntity getUserByUserId(Long userId) {
		return baseDao.getUserByUserId(userId);
	}

	private String sessionKey;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> login(LoginDTO dto) {
		log.info("code:{}", dto.getCode());
		try {
			WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(dto.getCode());
			sessionKey = sessionInfo.getSessionKey();
			log.info("openid:{}",sessionInfo.getOpenid());

			String openid = sessionInfo.getOpenid();
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

			return map;
		} catch (WxErrorException e) {
			log.error(e.getMessage(), e);
			throw new RenException(e.toString());
		}
		finally {
			WxMaConfigHolder.remove();//清理ThreadLocal
		}

	}

	@Override
	public String wxGetPhone(LoginDTO dto) {
		String code = dto.getCode();
		log.info("code:{}",code);
		// 解密
		log.info("dto:{}",dto );
		log.info("sessionKey:{}",sessionKey);
		try {
			WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getNewPhoneNoInfo(code);
			log.info("phoneNoInfo:{}",phoneNoInfo);

			String phoneNumber = phoneNoInfo.getPhoneNumber();
			WxMaConfigHolder.remove();//清理ThreadLocal

			TokenEntity tokenEntity = tokenService.getByToken(request.getHeader("token"));
			System.out.println("UserId" + tokenEntity.getUserId());
			UserEntity byId = userDao.selectById(tokenEntity.getUserId());

			log.info(byId.getPhone());
			//如果手机号为空，就更新手机号
			if(!StringUtils.hasText(byId.getPhone())){
				byId.setPhone(phoneNumber);
				userDao.updateById(byId);
			}
			return phoneNumber;
		} catch (WxErrorException e) {
			e.printStackTrace();
			throw new RenException("获取手机号失败");
		}
	}

}