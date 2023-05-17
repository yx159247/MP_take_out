package io.renren.modules.takeout.dao;

import io.renren.common.entity.DishEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜品管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Mapper
public interface DishDao extends BaseDao<DishEntity> {
	List<Long> selectDishesBySetmealId(List<Long> setmealId);

	int changeDishesStatusById(List<Long> dishId);

	List<Long> selectDishesByCategoryId(Long id);

}