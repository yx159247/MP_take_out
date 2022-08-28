package io.renren.common.service;

import io.renren.common.dto.CategoryDTO;
import io.renren.common.service.CrudService;

import io.renren.common.entity.CategoryEntity;

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