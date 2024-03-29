package io.renren.modules.takeout.dao;

import io.renren.common.entity.SetmealEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 套餐
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Mapper
public interface SetmealDao extends BaseDao<SetmealEntity> {
	List<Long> selectSetmealByCategoryId(Long categoryId);
}