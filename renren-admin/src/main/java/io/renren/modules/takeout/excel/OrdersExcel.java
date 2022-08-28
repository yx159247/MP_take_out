package io.renren.modules.takeout.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
public class OrdersExcel {
    @Excel(name = "主键")
    private Long id;
    @Excel(name = "订单号")
    private String number;
    @Excel(name = "订单状态 1待付款，2待派送，3已派送，4已完成，5已取消")
    private Integer status;
    @Excel(name = "下单用户")
    private Long userId;
    @Excel(name = "地址id")
    private Long addressBookId;
    @Excel(name = "下单时间")
    private Date orderTime;
    @Excel(name = "结账时间")
    private Date checkoutTime;
    @Excel(name = "支付方式 1微信,2支付宝")
    private Integer payMethod;
    @Excel(name = "实收金额")
    private BigDecimal amount;
    @Excel(name = "备注")
    private String remark;
    @Excel(name = "")
    private String phone;
    @Excel(name = "")
    private String address;
    @Excel(name = "")
    private String userName;
    @Excel(name = "")
    private String consignee;

}