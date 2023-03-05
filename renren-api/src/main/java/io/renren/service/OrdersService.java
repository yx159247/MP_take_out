package io.renren.service;

import io.renren.common.entity.OrdersEntity;
import io.renren.common.page.PageData;
import io.renren.dto.OrdersDTO;

import java.util.Map;

/**
 * 订单表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
public interface OrdersService extends CrudService<OrdersEntity, OrdersDTO> {


    /**
     * 微信小程序用户订单查询
     * @param params
     * @return
     */
    PageData<OrdersDTO> userPage(Map<String, Object> params);

    /**
     * 小程序提交订单
     * @param ordersDTO
     */
    void submit(OrdersDTO ordersDTO);

    /**
     * 小程序删除订单
     * @param id
     */
    void deleteOrder(Long id);
}