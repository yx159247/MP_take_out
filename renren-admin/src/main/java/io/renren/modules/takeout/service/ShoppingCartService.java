package io.renren.modules.takeout.service;

import io.renren.common.entity.ShoppingCartEntity;
import io.renren.modules.takeout.dto.ShoppingCartDTO;

import java.util.List;

/**
 * 购物车
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
public interface ShoppingCartService extends CrudService<ShoppingCartEntity, ShoppingCartDTO> {

    /**
     * 添加菜品或套餐进购物车
     * @param shoppingCartDTO
     * @return
     */
    ShoppingCartEntity add2shoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 从购物车删除菜品或套餐
     * @param shoppingCartDTO
     * @return
     */
    ShoppingCartEntity subFromShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void cleanShoppingCart();


    /**
     * 购物车列表
     * @return
     */
    List<ShoppingCartEntity> ShoppingCartList();


}