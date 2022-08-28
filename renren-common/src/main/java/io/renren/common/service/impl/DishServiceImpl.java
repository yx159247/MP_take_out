package io.renren.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.dto.DishDTO;
import io.renren.common.redis.RedisKeys;
import io.renren.common.dao.CategoryDao;
import io.renren.common.dao.DishDao;
import io.renren.common.dao.DishFlavorDao;

import io.renren.common.entity.CategoryEntity;
import io.renren.common.entity.DishEntity;
import io.renren.common.entity.DishFlavorEntity;
import io.renren.common.service.DishService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜品管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class DishServiceImpl extends CrudServiceImpl<DishDao, DishEntity, DishDTO>  implements DishService {

    @Autowired
    private DishDao dishDao;
    @Autowired
    private DishFlavorDao dishFlavorDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public QueryWrapper<DishEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<DishEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(id), "name", id);

        return wrapper;
    }

    @Override
    public DishDTO getByWithFlavor(Long id) {
        DishEntity dishEntity = dishDao.selectById(id);
        DishDTO DishDTO = new DishDTO();
        BeanUtils.copyProperties(dishEntity,DishDTO);
        LambdaQueryWrapper<DishFlavorEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavorEntity::getDishId,dishEntity.getId());
        List<DishFlavorEntity> dishFlavorEntityList = dishFlavorDao.selectList(queryWrapper);

        DishDTO.setFlavors(dishFlavorEntityList);
        return DishDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithFlavor(DishDTO dto) {
        DishEntity dishEntity = new DishEntity();
        BeanUtils.copyProperties(dto,dishEntity);
        dishDao.insert(dishEntity);
        Long dishId = dishEntity.getId();
        //菜品口味
        List<DishFlavorEntity> flavors = dto.getFlavors();
        for (DishFlavorEntity flavor : flavors) {
            flavor.setDishId(dishId);
            dishFlavorDao.insert(flavor);
        }

        //flavors = flavors.stream().map((item) ->{
        //    item.setDishId(dishId);
        //    return item;
        //}).collect(Collectors.toList());
        //dishFlavorService.saveBatch(flavors);
        String image = dto.getImage();
        redisTemplate.opsForSet().add(RedisKeys.getFoodPicDbResources(),image);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithFlavor(DishDTO dto) {
        DishEntity dishEntity = new DishEntity();
        BeanUtils.copyProperties(dto,dishEntity);
        dishDao.updateById(dishEntity);
        LambdaQueryWrapper<DishFlavorEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavorEntity::getDishId,dto.getId());
        dishFlavorDao.delete(queryWrapper);
        Long id = dishEntity.getId();
        List<DishFlavorEntity> flavors = dto.getFlavors();
        for (DishFlavorEntity flavor : flavors) {
            flavor.setDishId(id);
            dishFlavorDao.insert(flavor);
        }
        String image = dto.getImage();
        redisTemplate.opsForSet().add(RedisKeys.getFoodPicDbResources(),image);
    }

    @Override
    public List<DishDTO> findDishByCategoryId(DishDTO dishDTO) {

        List<DishDTO> dishDtoList =null;
        LambdaQueryWrapper<DishEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dishDTO.getCategoryId() != null,DishEntity::getCategoryId,dishDTO.getCategoryId());
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


        return dishDtoList;
    }
}