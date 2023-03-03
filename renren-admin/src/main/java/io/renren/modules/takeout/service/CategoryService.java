package io.renren.modules.takeout.service;

import io.renren.common.entity.CategoryEntity;
import io.renren.modules.takeout.dto.CategoryDTO;

import java.util.List;

/**
 * 菜品及套餐分类
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
public interface CategoryService extends CrudService<CategoryEntity, CategoryDTO> {

    List<CategoryDTO> findCategoryByQuery(CategoryDTO categoryDTO);
}