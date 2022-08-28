/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.redis.RedisUtils;
import io.renren.common.utils.DateUtils;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.common.dao.OrdersDao;
import io.renren.common.entity.OrdersEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private OrdersDao ordersDao;
    @Test
    public void contextLoads() {
        SysUserEntity user = new SysUserEntity();
        user.setEmail("123456@qq.com");
        redisUtils.set("user", user);

        System.out.println(ToStringBuilder.reflectionToString(redisUtils.get("user")));
    }

    @Test
    public void dateTest(){
        Date[] weekStartAndEnd = DateUtils.getWeekStartAndEnd(-1);
        System.out.println("weekStartAndEnd = " + weekStartAndEnd[0] + weekStartAndEnd[1]);

        Date date = DateUtils.addDateDays(new Date(), -1);
        DateUtils.format(date,DateUtils.DATE_PATTERN);
        System.out.println("date = " + date);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(-7);
        System.out.println("start = " + start);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).plusDays(-7);
        System.out.println("end = " + end);
        LocalDate startDay = LocalDate.now().plusDays(-7);
        System.out.println("startDay = " + startDay);

        Date startTime1 = org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);

        DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endOfDay = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -7));
        System.out.println("startTime = " + startTime);
        System.out.println("endOfDay = " + endOfDay);
        LambdaQueryWrapper<OrdersEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(OrdersEntity::getOrderTime,startTime,endOfDay);
        Long aLong = ordersDao.selectCount(queryWrapper);

        System.out.println("aLong = " + aLong);

        DateTime dateTime = DateUtil.offsetDay(startTime, 1);
        System.out.println("dateTime = " + dateTime);

        Date dateTime1 = DateUtil.offsetDay(DateUtil.date(), -7);
        String format = DateUtil.format(dateTime1, "yyyy-MM-dd");
        System.out.println("dateTime1 = " + format);
    }

    @Test
    public void OrderTest(){
        DateTime start = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime end = DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime startDay = DateUtil.offsetDay(DateUtil.date(), -7);

        Map<String,Object> map = new HashMap<>();
        List<Long> orderCount = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (int i = 0;i<7;i++){
            LambdaQueryWrapper<OrdersEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.between(OrdersEntity::getOrderTime,start,end);
            Long aLong = ordersDao.selectCount(queryWrapper);
            orderCount.add(aLong);
            start = DateUtil.offsetDay(start, 1);
            end = DateUtil.offsetDay(end, 1);
            String formatStartDay = DateUtil.format(startDay, "yyyy-MM-dd");
            days.add(formatStartDay);
            startDay = DateUtil.offsetDay(startDay, 1);
        }
        map.put("orderCount",orderCount);
        map.put("days",days);
        System.out.println("map = " + map);
    }



}