package io.renren.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.dto.OrdersDTO;
import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;

import io.renren.common.entity.OrdersEntity;

import java.util.Map;

/**
 * 订单表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
public interface OrdersService extends CrudService<OrdersEntity, OrdersDTO> {



    /**
     * 查询今日订单量
     * @return
     */
    long countToDayOrder();

    /**
     * 查询昨日订单量
     * @return
     */
    long countYesDayOrder();

    /**
     * 查询近一周流水
     * @return
     */
    Map OneWeekLiuShui();

    /**
     * 查询近一周订单数量
     * @return
     */
    Map OneWeekOrder();

    /**
     * 查询热卖套餐
     * @return
     */
    Map hotSeal();

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