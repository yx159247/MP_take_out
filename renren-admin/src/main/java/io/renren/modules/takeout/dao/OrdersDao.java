package io.renren.modules.takeout.dao;

import io.renren.common.entity.OrdersEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Mapper
public interface OrdersDao extends BaseDao<OrdersEntity> {
	
}