package io.renren.modules.takeout.dao;

import io.renren.common.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Mapper
public interface UserDao extends BaseDao<UserEntity> {

    UserEntity getUserByUserId(Long userId);

    UserEntity getUserByMobile(String mobile);
}