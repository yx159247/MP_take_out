package io.renren.modules.takeout.service;

import io.renren.common.entity.UserEntity;
import io.renren.modules.takeout.dto.UserDTO;

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