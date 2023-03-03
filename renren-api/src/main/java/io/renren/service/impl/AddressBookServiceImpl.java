package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.renren.common.entity.AddressBookEntity;
import io.renren.common.utils.UserUtils;
import io.renren.dao.AddressBookDao;
import io.renren.dto.AddressBookDTO;
import io.renren.service.AddressBookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 地址管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class AddressBookServiceImpl extends CrudServiceImpl<AddressBookDao, AddressBookEntity, AddressBookDTO> implements AddressBookService {

    @Autowired
    private AddressBookDao addressBookDao;
    @Autowired
    private UserUtils userUtils;

    @Override
    public QueryWrapper<AddressBookEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<AddressBookEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public AddressBookEntity findDefaultAddress(Long userId) {

        //查询默认地址
        LambdaQueryWrapper<AddressBookEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBookEntity::getUserId, userUtils.getUserId()).eq(AddressBookEntity::getIsDefault, 1);
        AddressBookEntity addressBookEntity = addressBookDao.selectOne(queryWrapper);
        //判断是否为空
        if (addressBookEntity == null) {
            return null;
        }
        //返回
        return addressBookEntity;
    }

    @Override
    public void UpdateAddressByUserId(AddressBookDTO addressBookDTO) {

        //通过用户id修改地址
        LambdaQueryWrapper<AddressBookEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBookEntity::getId, addressBookDTO.getId());
        AddressBookEntity addressBookEntity = new AddressBookEntity();
        BeanUtils.copyProperties(addressBookDTO, addressBookEntity);
        addressBookDao.update(addressBookEntity, queryWrapper);

    }

    @Override
    public AddressBookEntity saveAddress(AddressBookDTO addressBookDTO) {
        //获取用户ID
        Long userId = userUtils.getUserId();
        //将用户id添加进dto
        addressBookDTO.setUserId(userId);
        //创建一个地址实体
        AddressBookEntity addressBookEntity = new AddressBookEntity();
        //将dto的值赋值给实体
        BeanUtils.copyProperties(addressBookDTO, addressBookEntity);
        //添加地址
        addressBookDao.insert(addressBookEntity);
        //返回地址实体
        return addressBookEntity;

    }

    @Override
    public List<AddressBookEntity> findAddressByUserId(AddressBookEntity addressBookEntity) {


        //获取用户ID
        Long userId = userUtils.getUserId();
        //将用户id添加进实体
        addressBookEntity.setUserId(userId);
        //创建一个查询条件
        LambdaQueryWrapper<AddressBookEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBookEntity.getUserId()!=null,AddressBookEntity::getUserId,addressBookEntity.getUserId());
        queryWrapper.orderByDesc(AddressBookEntity::getCreateDate);

        //查询地址
        List<AddressBookEntity> addressBookEntities = addressBookDao.selectList(queryWrapper);

        //返回地址实体
        return addressBookEntities;

    }

    @Override
    public void setDefaultAddress(AddressBookDTO addressBookDTO) {

        //获取用户ID
        Long userId = userUtils.getUserId();
        //创建更新条件
        LambdaUpdateWrapper<AddressBookEntity> updateWrapper = new LambdaUpdateWrapper<>();
        //设置更新条件
        updateWrapper.eq(AddressBookEntity::getUserId, userId).set(AddressBookEntity::getIsDefault, 0);

        //创建一个地址实体
        AddressBookEntity addressBookEntity = new AddressBookEntity();
        //将dto的值赋值给实体
        BeanUtils.copyProperties(addressBookDTO, addressBookEntity);

        addressBookDao.update(addressBookEntity,updateWrapper);

        //将当前地址设置为默认地址
        addressBookEntity.setIsDefault(1);

        //更新地址
        addressBookDao.updateById(addressBookEntity);

    }
}