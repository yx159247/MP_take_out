package io.renren.modules.takeout.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单明细表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
public class OrderDetailExcel {
    @Excel(name = "主键")
    private Long id;
    @Excel(name = "名字")
    private String name;
    @Excel(name = "图片")
    private String image;
    @Excel(name = "订单id")
    private Long orderId;
    @Excel(name = "菜品id")
    private Long dishId;
    @Excel(name = "套餐id")
    private Long setmealId;
    @Excel(name = "口味")
    private String dishFlavor;
    @Excel(name = "数量")
    private Integer number;
    @Excel(name = "金额")
    private BigDecimal amount;

}