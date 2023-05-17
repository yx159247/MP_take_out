package io.renren.modules.takeout.dao;


import io.renren.modules.takeout.entity.AddressBookEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地址管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Mapper
public interface AddressBookDao extends BaseDao<AddressBookEntity> {
	
}