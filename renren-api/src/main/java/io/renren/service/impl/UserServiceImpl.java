package io.renren.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.entity.UserEntity;
import io.renren.common.exception.RenException;
import io.renren.common.redis.RedisUtils;
import io.renren.common.utils.UserUtils;
import io.renren.common.utils.ValidateCodeUtils;
import io.renren.dao.UserDao;
import io.renren.dto.LoginDTO;
import io.renren.dto.PhoneLoginDto;
import io.renren.dto.UpdateUserInfoDto;
import io.renren.entity.TokenEntity;
import io.renren.properties.WeixinProperties;
import io.renren.service.TokenService;
import io.renren.service.UserService;
import io.renren.vo.LoginVo;
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

/**
 * 用户信息
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
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
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserUtils userUtils;

    @Override
    public UserEntity getUserByUserId(Long userId) {
        return baseDao.getUserByUserId(userId);
    }

    private String sessionKey;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVo login(LoginDTO dto) {
        log.info("code:{}", dto.getCode());
        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(dto.getCode());
            sessionKey = sessionInfo.getSessionKey();
            log.info("openid:{}", sessionInfo.getOpenid());

            String openid = sessionInfo.getOpenid();
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getNewPhoneNoInfo(dto.getGetPhoneCode());
            log.info("phoneNoInfo:{}", phoneNoInfo);

            String phoneNumber = phoneNoInfo.getPhoneNumber();
            WxMaConfigHolder.remove();//清理ThreadLocal

            LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserEntity::getPhone, phoneNumber);
            UserEntity user = userDao.selectOne(queryWrapper);
            Map<String, Object> map = new HashMap<>();
            LoginVo loginVo = new LoginVo();
            if (user == null) {
                UserEntity userEntity = new UserEntity();
                userEntity.setOpenid(openid);
                userEntity.setCreateDate(new Date());
                userEntity.setPhone(phoneNumber);
                userDao.insert(userEntity);
                TokenEntity tokenEntity = tokenService.createToken(userEntity.getId());
                loginVo.setToken(tokenEntity.getToken());
                loginVo.setUserId(userEntity.getId());
                loginVo.setPhone(userEntity.getPhone());
            } else {
                TokenEntity tokenEntity = tokenService.createToken(user.getId());
                loginVo.setToken(tokenEntity.getToken());
                loginVo.setUserId(user.getId());
                loginVo.setPhone(phoneNumber);
                loginVo.setAvatarUrl(user.getAvatarUrl());
                loginVo.setNickName(user.getNickName());
            }

            return loginVo;
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            throw new RenException(e.toString());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }

    }

    @Override
    public String wxGetPhone(LoginDTO dto) {
        String code = dto.getCode();
        log.info("code:{}", code);
        // 解密
        log.info("dto:{}", dto);
        log.info("sessionKey:{}", sessionKey);
        try {
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getNewPhoneNoInfo(code);
            log.info("phoneNoInfo:{}", phoneNoInfo);

            String phoneNumber = phoneNoInfo.getPhoneNumber();
            WxMaConfigHolder.remove();//清理ThreadLocal

            TokenEntity tokenEntity = tokenService.getByToken(request.getHeader("token"));
            System.out.println("UserId" + tokenEntity.getUserId());
            UserEntity byId = userDao.selectById(tokenEntity.getUserId());

            log.info(byId.getPhone());
            //如果手机号为空，就更新手机号
            if (!StringUtils.hasText(byId.getPhone())) {
                byId.setPhone(phoneNumber);
                userDao.updateById(byId);
            }
            return phoneNumber;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new RenException("获取手机号失败");
        }
    }

    //@Override
    //@Transactional(rollbackFor = Exception.class)
    //public boolean updateUserInfo(UserDTO userDTO) {
    //    LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
    //    queryWrapper.eq(UserEntity::getId, userDTO.getId());
    //    UserEntity userEntity = userDao.selectOne(queryWrapper);
    //    userEntity.setAvatarUrl(userDTO.getAvatarUrl());
    //    userEntity.setNickName(userDTO.getNickName());
    //    int i = userDao.updateById(userEntity);
    //    return i > 0;
    //}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVo phoneLogin(PhoneLoginDto dto) {
        String phone = dto.getPhone();
        String code = dto.getCode();
        String codeFromRedis = (String) redisUtils.get(phone);
        if (codeFromRedis != null && codeFromRedis.equals(code)) {
            LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserEntity::getPhone, phone);
            UserEntity userEntity = userDao.selectOne(queryWrapper);
            LoginVo loginVo = new LoginVo();
            if (userEntity == null) {
                UserEntity user = new UserEntity();
                user.setPhone(phone);
                user.setCreateDate(new Date());
                userDao.insert(user);
                TokenEntity tokenEntity = tokenService.createToken(user.getId());
                loginVo.setToken(tokenEntity.getToken());
                loginVo.setUserId(user.getId());
                loginVo.setPhone(user.getPhone());
            } else {
                TokenEntity tokenEntity = tokenService.createToken(userEntity.getId());
                loginVo.setToken(tokenEntity.getToken());
                loginVo.setUserId(userEntity.getId());
                loginVo.setPhone(userEntity.getPhone());
                loginVo.setAvatarUrl(userEntity.getAvatarUrl());
                loginVo.setNickName(userEntity.getNickName());
            }
            return loginVo;
        } else {
            throw new RenException("验证码错误");
        }
    }

    @Override
    public boolean sendValidateCode(String phone) {
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        log.info("code:{}", code);
        redisUtils.set(phone, code, 60 * 5);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        Long userId = userUtils.getUserId();
        UserEntity userEntity = userDao.selectById(userId);

        String phone = updateUserInfoDto.getPhone();
        if (phone != null) {
            String code = updateUserInfoDto.getCode();
            String codeFromRedis = (String) redisUtils.get(phone);
            userEntity.setPhone(codeFromRedis != null && codeFromRedis.equals(code) ? phone : userEntity.getPhone());
        }

        String avatarUrl = updateUserInfoDto.getAvatarUrl();
        userEntity.setAvatarUrl(avatarUrl != null ? avatarUrl : userEntity.getAvatarUrl());

        String nickName = updateUserInfoDto.getNickName();
        userEntity.setNickName(nickName != null ? nickName : userEntity.getNickName());

        Integer gender = updateUserInfoDto.getGender();
        userEntity.setGender(gender != null ? gender : userEntity.getGender());
        String name = updateUserInfoDto.getName();
        userEntity.setName(name != null ? name : userEntity.getName());

        String idNumber = updateUserInfoDto.getIdNumber();
        userEntity.setIdNumber(idNumber != null ? idNumber : userEntity.getIdNumber());

        int res = userDao.updateById(userEntity);
        return res > 0;
    }

}