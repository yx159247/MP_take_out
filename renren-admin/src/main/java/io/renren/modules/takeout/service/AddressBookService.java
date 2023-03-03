package io.renren.modules.takeout.service;

import io.renren.common.entity.AddressBookEntity;
import io.renren.modules.takeout.dto.AddressBookDTO;

import java.util.List;

/**
 * 地址管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
public interface AddressBookService extends CrudService<AddressBookEntity, AddressBookDTO> {

    /**
     * 根据用户ID查找默认地址
     * @param userId
     * @return
     */
    AddressBookEntity findDefaultAddress(Long userId);

    /**
     * 根据用户ID修改地址
     * @param addressBookDTO
     */
    void UpdateAddressByUserId(AddressBookDTO addressBookDTO);

    /**
     * 添加地址
     * @param addressBookDTO
     * @return
     */
    AddressBookEntity saveAddress(AddressBookDTO addressBookDTO);


    /**
     * 根据用户ID查找地址列表
     * @param addressBookEntity
     * @return
     */
    List<AddressBookEntity> findAddressByUserId(AddressBookEntity addressBookEntity);

    /**
     * 设置默认地址
     * @param addressBookDTO
     */
    void setDefaultAddress(AddressBookDTO addressBookDTO);
}