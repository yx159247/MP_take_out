package io.renren.common.dao;

import io.renren.common.dao.BaseDao;
import io.renren.common.entity.DishEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜品管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Mapper
public interface DishDao extends BaseDao<DishEntity> {
	
}