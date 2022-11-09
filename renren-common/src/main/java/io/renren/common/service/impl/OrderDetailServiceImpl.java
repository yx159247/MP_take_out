package io.renren.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.util.concurrent.AtomicDouble;
import io.renren.common.dao.OrderDetailDao;
import io.renren.common.dto.OrderDetailDTO;

import io.renren.common.entity.OrderDetailEntity;
import io.renren.common.service.OrderDetailService;
import io.renren.common.vo.OrderDetailInfoVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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


    @Override
    public List<OrderDetailInfoVo> getByOrderId(Long id) {

        LambdaQueryWrapper<OrderDetailEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetailEntity::getOrderId,id);
        List<OrderDetailEntity> orderDetails = orderDetailDao.selectList(queryWrapper);
        AtomicDouble amount = new AtomicDouble(0);
        List<OrderDetailInfoVo> orderDetailInfoVos = orderDetails.stream().map(orderDetail -> {
            OrderDetailInfoVo orderDetailInfoVo = new OrderDetailInfoVo();
            BeanUtils.copyProperties(orderDetail, orderDetailInfoVo);
            amount.addAndGet(orderDetail.getAmount().multiply(new BigDecimal(orderDetail.getNumber())).doubleValue());
            orderDetailInfoVo.setAmount(BigDecimal.valueOf(amount.get()));
            amount.set(0);
            return orderDetailInfoVo;
        }).collect(Collectors.toList());
        return orderDetailInfoVos;
    }
}