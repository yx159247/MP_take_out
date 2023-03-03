package io.renren.service;

import io.renren.common.entity.OrderDetailEntity;

import io.renren.dto.OrderDetailDTO;

import java.util.List;

/**
 * 订单明细表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
public interface OrderDetailService extends CrudService<OrderDetailEntity, OrderDetailDTO> {

    /**
     * 根据订单Id查看订单详情
     * @param id
     * @return
     */

}