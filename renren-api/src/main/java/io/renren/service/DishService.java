package io.renren.service;

import io.renren.common.entity.DishEntity;
import io.renren.dto.DishDTO;

import java.util.List;

/**
 * 菜品管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
public interface DishService extends CrudService<DishEntity, DishDTO> {

    /**
     * 通过分类ID查找对应的菜品
     *
     * @param dishDTO
     * @return
     */
    List<DishDTO> findDishByCategoryId(DishDTO dishDTO);

    /**
     * 删除菜品
     *
     * @param aLong
     */
    @Override
    void delete(Long[] aLong);
}