package io.renren.modules.takeout.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.util.concurrent.AtomicDouble;
import io.renren.common.entity.*;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.utils.UserUtils;
import io.renren.modules.takeout.dao.*;
import io.renren.modules.takeout.dto.OrdersDTO;
import io.renren.modules.takeout.service.OrderDetailService;
import io.renren.modules.takeout.service.OrdersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class OrdersServiceImpl extends CrudServiceImpl<OrdersDao, OrdersEntity, OrdersDTO> implements OrdersService {

    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private ShoppingCartDao shoppingCartDao;
    @Autowired
    private AddressBookDao addressBookDao;
    @Autowired
    private OrderDetailService orderDetailService;


    @Override
    public QueryWrapper<OrdersEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String beginTime = (String) params.get("beginTime");
        String endTime = (String) params.get("endTime");


        QueryWrapper<OrdersEntity> wrapper = new QueryWrapper<>();

        wrapper.like(StringUtils.isNotBlank(id), "id", id);
        wrapper.between(StringUtils.isNotEmpty(beginTime) ||
                StringUtils.isNotEmpty(endTime), "order_time", beginTime, endTime);


        return wrapper;
    }


    @Override
    public long countToDayOrder() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        LambdaQueryWrapper<OrdersEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(OrdersEntity::getOrderTime, start, end);
        Long aLong = ordersDao.selectCount(queryWrapper);
        return aLong;
    }

    @Override
    public long countYesDayOrder() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(-1);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).plusDays(-1);
        LambdaQueryWrapper<OrdersEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(OrdersEntity::getOrderTime, start, end);
        Long aLong = ordersDao.selectCount(queryWrapper);
        return aLong;
    }

    @Override
    public Map OneWeekLiuShui() {

        DateTime start = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime end = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime startDay = DateUtil.offsetDay(DateUtil.date(), -7);

        Map<String, Object> map = new HashMap<>();
        List<Double> amount = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LambdaQueryWrapper<OrdersEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.between(OrdersEntity::getOrderTime, start, end);
            List<OrdersEntity> orders = ordersDao.selectList(queryWrapper);
            double orderAmount = 0;
            for (OrdersEntity order : orders) {
                orderAmount += order.getAmount().doubleValue();
            }
            amount.add(orderAmount);
            start = DateUtil.offsetDay(start, 1);
            end = DateUtil.offsetDay(end, 1);
            String formatStartDay = DateUtil.format(startDay, "yyyy-MM-dd");
            days.add(formatStartDay);
            startDay = DateUtil.offsetDay(startDay, 1);

        }
        map.put("amount", amount);
        map.put("days", days);
        return map;
    }

    @Override
    public Map OneWeekOrder() {

        DateTime start = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime end = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime startDay = DateUtil.offsetDay(DateUtil.date(), -7);

        Map<String, Object> map = new HashMap<>();
        List<Long> orderCount = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LambdaQueryWrapper<OrdersEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.between(OrdersEntity::getOrderTime, start, end);
            Long aLong = ordersDao.selectCount(queryWrapper);
            orderCount.add(aLong);
            start = DateUtil.offsetDay(start, 1);
            end = DateUtil.offsetDay(end, 1);
            String formatStartDay = DateUtil.format(startDay, "yyyy-MM-dd");
            days.add(formatStartDay);
            startDay = DateUtil.offsetDay(startDay, 1);
        }
        map.put("orderCount", orderCount);
        map.put("days", days);
        return map;
    }

    @Override
    public Map hotSeal() {
        Map<String, Object> data = new HashMap<>();

        QueryWrapper<OrderDetailEntity> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.select("COUNT(dish_id) as dishCount,name ").groupBy("name").having("COUNT(dish_id)").orderByDesc("dishCount").last("LIMIT 2");
        List<Map<String, Object>> orderDetails = orderDetailDao.selectMaps(detailQueryWrapper);
        //count
        List<String> dishCount = new ArrayList<>();
        for (Map<String, Object> orderDetail : orderDetails) {
            String count = String.valueOf(orderDetail.get("dishCount"));
            dishCount.add(count);
        }
        data.put("dishCount", orderDetails);

        QueryWrapper<OrderDetailEntity> detailQueryWrapper1 = new QueryWrapper<>();
        detailQueryWrapper1.select("COUNT(setmeal_id) as setmealCount,name ").groupBy("name").having("COUNT(setmeal_id)").orderByDesc("setmealCount").last("LIMIT 2");
        List<Map<String, Object>> setmealDetails = orderDetailDao.selectMaps(detailQueryWrapper1);
        //count
        List<String> setmealCount = new ArrayList<>();
        for (Map<String, Object> setmealDetail : setmealDetails) {
            String count = String.valueOf(setmealDetail.get("setmealCount"));
            setmealCount.add(count);
        }
        data.put("setmealCount", setmealDetails);

        return data;
    }



}