package io.renren.dao;

import io.renren.common.entity.DishFlavorEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜品口味关系表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Mapper
public interface DishFlavorDao extends BaseDao<DishFlavorEntity> {
	
}