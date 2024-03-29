package io.renren.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.entity.SetmealDishEntity;
import io.renren.common.entity.SetmealEntity;
import io.renren.common.exception.RenException;
import io.renren.common.redis.RedisKeys;
import io.renren.common.redis.RedisUtils;
import io.renren.common.utils.ConvertUtils;
import io.renren.dao.OrdersDao;
import io.renren.dao.SetmealDao;
import io.renren.dao.SetmealDishDao;
import io.renren.dto.SetmealDTO;
import io.renren.service.SetmealService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 套餐
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class SetmealServiceImpl extends CrudServiceImpl<SetmealDao, SetmealEntity, SetmealDTO> implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private SetmealDishDao setmealDishDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private OrdersDao ordersDao;
    @Override
    public QueryWrapper<SetmealEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<SetmealEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public SetmealDTO getByWithDish(Long id) {
        SetmealEntity setmealEntity = setmealDao.selectById(id);
        SetmealDTO setmealDto = new SetmealDTO();
        BeanUtils.copyProperties(setmealEntity, setmealDto);
        LambdaQueryWrapper<SetmealDishEntity> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetmealDishEntity::getSetmealId, setmealEntity.getId());
        List<SetmealDishEntity> setmealDishes = setmealDishDao.selectList(dishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        //setmealMapper.selectById(id);

        return setmealDto;
    }

    @Override
    public List<SetmealDTO> findSetmealByCategoryId(SetmealDTO setmealDTO) {

        List<SetmealDTO> setmealDTOList = null;

        String key = RedisKeys.getSetmealCacheKey()+ ":"+ "setmeal_" + setmealDTO.getCategoryId() + "_" + setmealDTO.getStatus();
        //先从缓存中取
        setmealDTOList = (List<SetmealDTO>) redisUtils.get(key);

        //如果缓存有,直接返回数据
        if (setmealDTOList != null) {
            return setmealDTOList;
        }

        LambdaQueryWrapper<SetmealEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmealDTO.getCategoryId() != null, SetmealEntity::getCategoryId, setmealDTO.getCategoryId());
        queryWrapper.eq(setmealDTO.getStatus() != null, SetmealEntity::getStatus, setmealDTO.getStatus());
        queryWrapper.orderByDesc(SetmealEntity::getUpdateDate);
        List<SetmealEntity> setmealEntityList= setmealDao.selectList(queryWrapper);
        setmealDTOList = new ArrayList<>();
        for (SetmealEntity setmealEntity : setmealEntityList) {
            SetmealDTO setmealDTO1 = new SetmealDTO();
            BeanUtils.copyProperties(setmealEntity, setmealDTO1);
            int year = DateUtil.year(new Date());
            int month = DateUtil.month(new Date());
            month += 1;
            int mouthSales = ordersDao.getSetmealMonthSales(setmealEntity.getId(), year, month);
            setmealDTO1.setSales(mouthSales);
            setmealDTOList.add(setmealDTO1);
        }


        //存入缓存
        redisUtils.set(key, setmealDTOList,RedisUtils.HOUR_ONE_EXPIRE);

        return setmealDTOList;
    }

    @Override
    public void delete(Long[] ids) {

        setmealDao.selectBatchIds(Arrays.asList(ids)).forEach(setmealEntity -> {
            if (setmealEntity.getStatus() == 1) {
                throw new RenException("套餐已上架，不能删除");
            }
            //删除套餐实体
            setmealDao.deleteById(setmealEntity.getId());

            //删除套餐实体中的SetmealDishes
            LambdaQueryWrapper<SetmealDishEntity> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishLambdaQueryWrapper.eq(SetmealDishEntity::getSetmealId, setmealEntity.getId());
            redisTemplate.opsForSet().remove(RedisKeys.getFoodPicDbResources(), setmealEntity.getImage());
            setmealDishDao.delete(dishLambdaQueryWrapper);
            //删除缓存
            String key = RedisKeys.getSetmealCacheKey()+ ":"+ "setmeal_" + setmealEntity.getCategoryId() + "_" + setmealEntity.getStatus();
            redisUtils.delete(key);
        });

    }
}