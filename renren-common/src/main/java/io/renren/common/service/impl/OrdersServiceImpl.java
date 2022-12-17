package io.renren.common.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.util.concurrent.AtomicDouble;
import io.renren.common.constant.Constant;
import io.renren.common.dao.*;
import io.renren.common.dto.OrdersDTO;

import io.renren.common.entity.*;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.service.OrderDetailService;
import io.renren.common.service.OrdersService;
import io.renren.common.utils.Result;
import io.renren.common.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public PageData<OrdersDTO> userPage(Map<String, Object> params) {

        try {


            IPage page = baseDao.selectPage(
                    getPage(params, "order_time", false),
                    getWrapper(params).eq(StringUtils.isNotBlank(userUtils.getUserId().toString()), "user_id", userUtils.getUserId())
            );
            List<OrdersEntity> records = page.getRecords();
            System.out.println(records);
            List<OrdersDTO> ordersDtoList = new ArrayList<>();
            for (OrdersEntity record : records) {
                OrdersDTO ordersDto = new OrdersDTO();
                BeanUtils.copyProperties(record, ordersDto);
                Long userId = record.getUserId();
                LambdaQueryWrapper<OrderDetailEntity> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(OrderDetailEntity::getOrderId, record.getId());
                List<OrderDetailEntity> orderDetails = orderDetailDao.selectList(queryWrapper1);
                ordersDto.setOrderDetails(orderDetails);

                UserEntity user = userDao.selectById(userId);

                if (user != null) {
                    String name = user.getNickName();
                    ordersDto.setUserName(name);
                }
                ordersDtoList.add(ordersDto);
            }
            page.setRecords(ordersDtoList);
            return getPageData(page, OrdersDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException("订单数据加载失败");
        }
    }

    @Override
    public void submit(OrdersDTO ordersDTO) {
        //获得当前用户id
        Long userId = userUtils.getUserId();

        //查询当前用户购物车数据
        LambdaQueryWrapper<ShoppingCartEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCartEntity::getUserId, userId);
        List<ShoppingCartEntity> shoppingCarts = shoppingCartDao.selectList(queryWrapper);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new RenException("购物车为空，不能下单！");
        }
        //查询用户数据
        UserEntity user = userDao.selectById(userId);
        //查询地址数据
        Long addressBookId = ordersDTO.getAddressBookId();
        AddressBookEntity addressBook = addressBookDao.selectById(addressBookId);
        if (addressBook == null) {
            throw new RenException("地址信息有错误，不能下单！");
        }

        //向订单表插入数据，一条数据
        long orderId = IdWorker.getId();
        AtomicDouble amount = new AtomicDouble(0);

        List<OrderDetailEntity> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).doubleValue());
            return orderDetail;
        }).collect(Collectors.toList());


        ordersDTO.setId(orderId);
        ordersDTO.setOrderTime(new Date());
        ordersDTO.setCheckoutTime(new Date());
        ordersDTO.setStatus(2);
        //总金额
        ordersDTO.setAmount(new BigDecimal(amount.get()));
        ordersDTO.setUserId(userId);
        ordersDTO.setNumber(String.valueOf(orderId));
        ordersDTO.setUserName(user.getName());
        ordersDTO.setConsignee(addressBook.getConsignee());
        ordersDTO.setPhone(addressBook.getPhone());
        ordersDTO.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        OrdersEntity orders = new OrdersEntity();
        BeanUtils.copyProperties(ordersDTO, orders);
        ordersDao.insert(orders);

        //向订单明细表插入数据，多条数据
        //orderDetailService.saveBatch(orderDetails);
        orderDetailService.insertBatch(orderDetails);


        //清空购物车数据
        shoppingCartDao.delete(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(Long id) {
        OrdersEntity orders = ordersDao.selectById(id);
        if (orders == null) {
            throw new RenException("订单不存在！");
        }
        if (orders.getStatus() != 4 || orders.getStatus() != 5) {
            throw new RenException("订单状态不正确，不能删除！");
        }
        ordersDao.deleteById(id);
        orderDetailDao.delete(new LambdaQueryWrapper<OrderDetailEntity>().eq(OrderDetailEntity::getOrderId, id));
    }
}