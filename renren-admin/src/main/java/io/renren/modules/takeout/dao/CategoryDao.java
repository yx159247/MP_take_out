package io.renren.modules.takeout.dao;

import io.renren.common.entity.CategoryEntity;
import io.renren.common.entity.DishEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜品及套餐分类
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Mapper
public interface CategoryDao extends BaseDao<CategoryEntity> {

    int selectTypeById(Long id);

}