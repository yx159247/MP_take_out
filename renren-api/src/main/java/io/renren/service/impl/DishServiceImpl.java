package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.entity.CategoryEntity;
import io.renren.common.entity.DishEntity;
import io.renren.common.entity.DishFlavorEntity;
import io.renren.common.entity.SetmealDishEntity;
import io.renren.common.exception.RenException;
import io.renren.common.redis.RedisKeys;
import io.renren.common.redis.RedisUtils;
import io.renren.dao.CategoryDao;
import io.renren.dao.DishDao;
import io.renren.dao.DishFlavorDao;
import io.renren.dao.SetmealDishDao;
import io.renren.dto.DishDTO;
import io.renren.service.DishService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 菜品管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class DishServiceImpl extends CrudServiceImpl<DishDao, DishEntity, DishDTO> implements DishService {

    @Autowired
    private DishDao dishDao;
    @Autowired
    private DishFlavorDao dishFlavorDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private SetmealDishDao setmealDishDao;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public QueryWrapper<DishEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<DishEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(id), "name", id);

        return wrapper;
    }





    @Override
    public List<DishDTO> findDishByCategoryId(DishDTO dishDTO) {

        List<DishDTO> dishDtoList =null;

        String key = RedisKeys.getDishCacheKey()+ ":"+ "dish_" + dishDTO.getCategoryId() + "_" + dishDTO.getStatus();
        //先从缓存中取
        dishDtoList = (List<DishDTO>) redisUtils.get(key);

        //如果缓存有,直接返回数据
        if (dishDtoList != null) {
            return dishDtoList;
        }


        LambdaQueryWrapper<DishEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dishDTO.getCategoryId() != null,DishEntity::getCategoryId,dishDTO.getCategoryId());
        queryWrapper.eq(dishDTO.getStatus() != null,DishEntity::getStatus,dishDTO.getStatus());
        queryWrapper.orderByDesc(DishEntity::getSort).orderByDesc(DishEntity::getUpdateDate);
        List<DishEntity> dishes = dishDao.selectList(queryWrapper);
        dishDtoList = new ArrayList<>();

        for (DishEntity dish1 : dishes) {
            DishDTO DishDTO = new DishDTO();
            BeanUtils.copyProperties(dish1,DishDTO);
            Long categoryId = dish1.getCategoryId();
            CategoryEntity category = categoryDao.selectById(categoryId);
            if (category != null){
                String name = category.getName();
                DishDTO.setCategoryName(name);
            }

            //当前菜品ID
            Long dish1Id = dish1.getId();
            LambdaQueryWrapper<DishFlavorEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavorEntity::getDishId,dish1Id);
            List<DishFlavorEntity> dishFlavors = dishFlavorDao.selectList(lambdaQueryWrapper);
            DishDTO.setFlavors(dishFlavors);
            dishDtoList.add(DishDTO);

        }

        //存入缓存
        redisUtils.set(key,dishDtoList,RedisUtils.HOUR_ONE_EXPIRE);

        return dishDtoList;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        dishDao.selectBatchIds(Arrays.asList(ids)).forEach(item -> {
            if (item.getStatus() == 1){
                throw new RenException("该菜品为售卖状态，不能删除");
            }
            dishFlavorDao.selectList(new LambdaQueryWrapper<DishFlavorEntity>().eq(DishFlavorEntity::getDishId,item.getId())).forEach(flavor -> {
                dishFlavorDao.deleteById(flavor.getId());
            });

            LambdaQueryWrapper<SetmealDishEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDishEntity::getDishId,item.getId());
            Long count  = setmealDishDao.selectCount(queryWrapper);
            if (count > 0){
                throw new RenException("该菜品已经被套餐使用，不能删除,请先在套餐中删除菜品!");
            }
            redisTemplate.opsForSet().remove(RedisKeys.getFoodPicDbResources(),item.getImage());
            dishDao.deleteById(item.getId());

            //删除缓存
            String key = RedisKeys.getDishCacheKey()+ ":"+ "dish_" + item.getCategoryId() + "_" + item.getStatus();
            redisUtils.delete(key);
        });
    }
}