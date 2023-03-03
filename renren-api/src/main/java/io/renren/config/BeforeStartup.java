package io.renren.config;

import io.renren.common.entity.CategoryEntity;
import io.renren.common.redis.RedisKeys;
import io.renren.common.redis.RedisUtils;
import io.renren.dao.CategoryDao;
import io.renren.dto.DishDTO;
import io.renren.dto.SetmealDTO;
import io.renren.service.DishService;
import io.renren.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/3/1
 **/
@Configuration
@Slf4j
public class BeforeStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 是否开启redis缓存  true开启   false关闭
     */
    @Value("${renren.redis.open: false}")
    private boolean open;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (open){
            log.info("开启redis缓存");
            DishDTO dishDTO = new DishDTO();
            SetmealDTO setmealDTO = new SetmealDTO();
            setmealDTO.setStatus(1);
            dishDTO.setStatus(1);
            //查询菜品信息
            List<DishDTO> dish = dishService.findDishByCategoryId(dishDTO);
            //查询套餐信息
            List<SetmealDTO> setmeal = setmealService.findSetmealByCategoryId(setmealDTO);
            List<CategoryEntity> categoryEntityList = categoryDao.selectList(null);
            redisUtils.deleteKeysWithPrefix("dishCache:");
            redisUtils.deleteKeysWithPrefix("setmealCache:");

            Map<Integer, String> keyPrefixMap = new HashMap<>();
            keyPrefixMap.put(1, RedisKeys.getDishCacheKey() + ":" + "dish_");
            keyPrefixMap.put(2, RedisKeys.getSetmealCacheKey() + ":" + "setmeal_");

            for (CategoryEntity categoryEntity : categoryEntityList) {
                Long id = categoryEntity.getId();
                Integer type = categoryEntity.getType();
                String keyPrefix = keyPrefixMap.get(type);

                if (keyPrefix == null) {
                    // Handle invalid type
                    continue;
                }

                String key = keyPrefix + id + "_" + (type == 1 ? dishDTO.getStatus() : setmealDTO.getStatus());
                List<?> list = type == 1 ? dish : setmeal;
                List<?> filteredList;
                if (type == 1) {
                    List<DishDTO> dishList = (List<DishDTO>) list;
                    filteredList = dishList.stream().filter(dishDTO1 -> dishDTO1.getCategoryId().equals(id)).collect(Collectors.toList());
                } else {
                    List<SetmealDTO> setmealList = (List<SetmealDTO>) list;
                    filteredList = setmealList.stream().filter(setmealDTO1 -> setmealDTO1.getCategoryId().equals(id)).collect(Collectors.toList());
                }

                redisUtils.set(key, filteredList, RedisUtils.HOUR_ONE_EXPIRE);
                log.info(type == 1 ? "菜品" + "分类Id" + id + "初始化缓存成功" : "套餐" + "分类Id" + id + "初始化缓存成功");
            }
        } else {
            log.info("未启动redis缓存");
        }





    }
}
