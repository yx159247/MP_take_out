package io.renren.common.service;

import io.renren.common.dto.UserDTO;
import io.renren.common.service.CrudService;

import io.renren.common.entity.UserEntity;

/**
 * 用户信息
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
public interface UserService extends CrudService<UserEntity, UserDTO> {


    /**
     * 获取会员总数
     * @return
     */
    long userCount();
}