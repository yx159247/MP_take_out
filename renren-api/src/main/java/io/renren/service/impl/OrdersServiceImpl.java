package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.util.concurrent.AtomicDouble;
import io.renren.common.entity.*;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.utils.UserUtils;
import io.renren.dao.*;
import io.renren.dto.OrdersDTO;
import io.renren.service.OrderDetailService;
import io.renren.service.OrdersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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