package io.renren.modules.takeout.dao;

import io.renren.common.entity.OrderDetailEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单明细表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Mapper
public interface OrderDetailDao extends BaseDao<OrderDetailEntity> {
	
}