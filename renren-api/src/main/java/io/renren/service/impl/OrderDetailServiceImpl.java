package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.util.concurrent.AtomicDouble;
import io.renren.common.entity.OrderDetailEntity;

import io.renren.dao.OrderDetailDao;
import io.renren.dto.OrderDetailDTO;
import io.renren.service.OrderDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单明细表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class OrderDetailServiceImpl extends CrudServiceImpl<OrderDetailDao, OrderDetailEntity, OrderDetailDTO> implements OrderDetailService {

    @Autowired
    private OrderDetailDao orderDetailDao;
    @Override
    public QueryWrapper<OrderDetailEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<OrderDetailEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "order_id", id);

        return wrapper;
    }

}