package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.entity.SetmealDishEntity;
import io.renren.dao.SetmealDishDao;
import io.renren.dto.SetmealDishDTO;
import io.renren.service.SetmealDishService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 套餐菜品关系
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class SetmealDishServiceImpl extends CrudServiceImpl<SetmealDishDao, SetmealDishEntity, SetmealDishDTO> implements SetmealDishService {

    @Override
    public QueryWrapper<SetmealDishEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SetmealDishEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}