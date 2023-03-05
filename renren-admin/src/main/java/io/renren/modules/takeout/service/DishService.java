package io.renren.modules.takeout.service;

import io.renren.common.entity.DishEntity;
import io.renren.common.page.PageData;
import io.renren.modules.takeout.dto.DishDTO;

import java.util.List;
import java.util.Map;

/**
 * 菜品管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
public interface DishService extends CrudService<DishEntity, DishDTO> {

    /**
     * 根据菜品ID查口味列表
     *
     * @param id
     * @return
     */
    DishDTO getByWithFlavor(Long id);

    /**
     * 新增菜品，同时插入菜品对应的口味
     *
     * @param dto
     */
    void saveWithFlavor(DishDTO dto);

    /**
     * 修改菜品，同时修改菜品对应的口味
     *
     * @param dto
     */
    void updateWithFlavor(DishDTO dto);

    /**
     * 通过分类ID查找对应的菜品
     *
     * @param dishDTO
     * @return
     */
    List<DishDTO> findDishByCategoryId(DishDTO dishDTO);

    /**
     * 更新菜品状态
     *
     * @param ids
     */
    void updateStatus(Long[] ids);

    /**
     * 删除菜品
     *
     * @param aLong
     */
    @Override
    void delete(Long[] aLong);

    PageData<DishDTO> page(Map<String, Object> params);
}