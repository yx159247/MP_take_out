package io.renren.modules.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.entity.DishFlavorEntity;
import io.renren.modules.takeout.dao.DishFlavorDao;
import io.renren.modules.takeout.dto.DishFlavorDTO;
import io.renren.modules.takeout.service.DishFlavorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 菜品口味关系表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class DishFlavorServiceImpl extends CrudServiceImpl<DishFlavorDao, DishFlavorEntity, DishFlavorDTO> implements DishFlavorService {

    @Override
    public QueryWrapper<DishFlavorEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<DishFlavorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}