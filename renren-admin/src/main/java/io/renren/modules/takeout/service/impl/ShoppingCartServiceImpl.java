package io.renren.modules.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.entity.ShoppingCartEntity;
import io.renren.common.utils.UserUtils;
import io.renren.modules.takeout.dao.ShoppingCartDao;
import io.renren.modules.takeout.dto.ShoppingCartDTO;
import io.renren.modules.takeout.service.ShoppingCartService;
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

}