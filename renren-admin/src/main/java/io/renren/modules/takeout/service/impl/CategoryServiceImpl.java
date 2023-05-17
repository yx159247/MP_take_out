package io.renren.modules.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.renren.common.entity.CategoryEntity;
import io.renren.common.entity.DishEntity;
import io.renren.common.entity.SetmealEntity;
import io.renren.common.redis.RedisKeys;
import io.renren.common.redis.RedisUtils;
import io.renren.modules.takeout.dao.CategoryDao;
import io.renren.modules.takeout.dao.DishDao;
import io.renren.modules.takeout.dao.DishFlavorDao;
import io.renren.modules.takeout.dao.SetmealDao;
import io.renren.modules.takeout.dto.CategoryDTO;
import io.renren.modules.takeout.service.CategoryService;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜品及套餐分类
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class CategoryServiceImpl extends CrudServiceImpl<CategoryDao, CategoryEntity, CategoryDTO> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private DishDao dishDao;
    @Autowired
    private DishFlavorDao dishFlavorDao;
    @Autowired
    private RedisUtils redisUtils;
    @Override
    public QueryWrapper<CategoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<CategoryDTO> findCategoryByQuery(CategoryDTO categoryDTO) {
        LambdaQueryWrapper<CategoryEntity> queryWrapper  = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryDTO.getType() != null,CategoryEntity::getType,categoryDTO.getType());
        queryWrapper.orderByAsc(CategoryEntity::getSort).orderByDesc(CategoryEntity::getUpdateDate);
        List<CategoryDTO> categoryDTOList = categoryDao.selectList(queryWrapper).stream().map(categoryEntity -> {
            CategoryDTO dto = new CategoryDTO();
            BeanUtils.copyProperties(categoryEntity, dto);
            return dto;
        }).collect(Collectors.toList());

        return categoryDTOList;
    }

    @Override
    @Transactional
    public void delete(Long[] ids) {
        LambdaQueryWrapper<DishEntity> dishEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //1 根据id查询是套餐还是菜品分类
        for (Long id : ids) {
            int type = categoryDao.selectTypeById(id);
            if (type == 2){
                // 当前分类为套餐分类,根据分类id 查询出所关联的套餐
                List<Long> setmealEntities = setmealDao.selectSetmealByCategoryId(id);
                if (setmealEntities.size() > 0){
                    //根据套餐id查询关联的菜品
                    List<Long> dishes = dishDao.selectDishesBySetmealId(setmealEntities);

                    //停售菜品
                    dishDao.changeDishesStatusById(dishes);

                    //删除菜品
                    dishDao.deleteBatchIds(dishes);
                    //删除套餐
                    setmealDao.deleteBatchIds(setmealEntities);
                    //删除分类
                    categoryDao.deleteById(id);
                }else {
                    //删除分类
                    categoryDao.deleteById(id);
                }
                //删除缓存
                String key = RedisKeys.getSetmealCacheKey()+ ":"+ "setmeal_" + id + "_" + 0;
                redisUtils.delete(key);

            }else {
                // 当前分类为菜品分类
                //根据分类id查询出关联的菜品
                List<Long> dishes = dishDao.selectDishesByCategoryId(id);
                //停售菜品
                if (dishes.size() > 0){
                    dishDao.changeDishesStatusById(dishes);
                    //根据菜品id 查询出对应的口味并删除
                    dishFlavorDao.deleteDishFlavorByDishId(dishes);

                    //删除菜品
                    dishDao.deleteBatchIds(dishes);
                    //删除分类
                    categoryDao.deleteById(id);
                }else {
                    //删除分类
                    categoryDao.deleteById(id);
                }
                //删除缓存
                String key = RedisKeys.getDishCacheKey()+ ":"+ "dish_" + id + "_" + 0;
                redisUtils.delete(key);


            }

        }
    }
}