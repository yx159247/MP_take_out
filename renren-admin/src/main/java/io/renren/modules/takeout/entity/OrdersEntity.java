package io.renren.modules.takeout.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@TableName("orders")
public class OrdersEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 订单号
     */
	private String number;
    /**
     * 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
     */
	private Integer status;
    /**
     * 下单用户
     */
	private Long userId;
    /**
     * 地址id
     */
	private Long addressBookId;
    /**
     * 下单时间
     */
	private Date orderTime;
    /**
     * 结账时间
     */
	private Date checkoutTime;
    /**
     * 支付方式 1微信,2支付宝
     */
	private Integer payMethod;
    /**
     * 实收金额
     */
	private BigDecimal amount;
    /**
     * 备注
     */
	private String remark;
    /**
     * 
     */
	private String phone;
    /**
     * 
     */
	private String address;
    /**
     * 
     */
	private String userName;
    /**
     * 
     */
	private String consignee;
}