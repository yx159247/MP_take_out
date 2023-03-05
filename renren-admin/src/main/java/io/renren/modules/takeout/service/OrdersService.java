package io.renren.modules.takeout.service;

import io.renren.common.entity.OrdersEntity;
import io.renren.common.page.PageData;
import io.renren.modules.takeout.dto.OrdersDTO;

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


}