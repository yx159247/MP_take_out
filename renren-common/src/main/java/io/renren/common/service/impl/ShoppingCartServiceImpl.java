package io.renren.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.dao.ShoppingCartDao;
import io.renren.common.dto.ShoppingCartDTO;

import io.renren.common.entity.ShoppingCartEntity;
import io.renren.common.service.ShoppingCartService;
import io.renren.common.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 购物车
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class ShoppingCartServiceImpl extends CrudServiceImpl<ShoppingCartDao, ShoppingCartEntity, ShoppingCartDTO> implements ShoppingCartService {

    @Autowired
    private ShoppingCartDao shoppingCartDao;
    @Autowired
    private UserUtils userUtils;

    @Override
    public QueryWrapper<ShoppingCartEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ShoppingCartEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "user_id", id);

        return wrapper;
    }


    @Override
    public ShoppingCartEntity add2shoppingCart(ShoppingCartDTO shoppingCartDTO) {

        Long userId = userUtils.getUserId();
        shoppingCartDTO.setUserId(userId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCartDTO.getDishId();
        LambdaQueryWrapper<ShoppingCartEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCartEntity::getUserId,userId);

        if (dishId != null){
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCartEntity::getDishId,dishId);
        }else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCartEntity::getSetmealId,shoppingCartDTO.getSetmealId());
        }
        ShoppingCartEntity shoppingCart1 = shoppingCartDao.selectOne(queryWrapper);


        //如果已经存在，就在原来的基础上+1
        if (shoppingCart1 != null){
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number+1);
            shoppingCartDao.updateById(shoppingCart1);
        }else {
            //如果不存在，则添加到购物车，数量默认就是1
            ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
            
            BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
            shoppingCart.setNumber(1);
            shoppingCartDao.insert(shoppingCart);
            shoppingCart1 = shoppingCart;
        }
        return shoppingCart1;
    }

    @Override
    public ShoppingCartEntity subFromShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        Long userId = userUtils.getUserId();

        LambdaQueryWrapper<ShoppingCartEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCartDTO.getDishId() != null,ShoppingCartEntity::getDishId,shoppingCartDTO.getDishId());
        queryWrapper.eq(shoppingCartDTO.getSetmealId() != null,ShoppingCartEntity::getSetmealId,shoppingCartDTO.getSetmealId());
        queryWrapper.eq(ShoppingCartEntity::getUserId,userId);
        ShoppingCartEntity one = shoppingCartDao.selectOne(queryWrapper);
        if (one.getNumber() == 1){
            shoppingCartDao.deleteById(one);
        }
        Integer number = one.getNumber();
        one.setNumber(number-1);
        shoppingCartDao.updateById(one);
        return one;
    }

    @Override
    public void cleanShoppingCart() {
        Long id = userUtils.getUserId();
        LambdaQueryWrapper<ShoppingCartEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCartEntity::getUserId,id);
        shoppingCartDao.delete(queryWrapper);
    }

    @Override
    public List<ShoppingCartEntity> ShoppingCartList() {
        Long id = userUtils.getUserId();
        LambdaQueryWrapper<ShoppingCartEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCartEntity::getUserId,id);
        queryWrapper.orderByAsc(ShoppingCartEntity::getCreateDate);
        return shoppingCartDao.selectList(queryWrapper);
    }
}