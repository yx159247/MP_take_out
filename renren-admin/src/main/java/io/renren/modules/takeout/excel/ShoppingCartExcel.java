package io.renren.modules.takeout.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
public class ShoppingCartExcel {
    @Excel(name = "主键")
    private Long id;
    @Excel(name = "名称")
    private String name;
    @Excel(name = "图片")
    private String image;
    @Excel(name = "主键")
    private Long userId;
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
    @Excel(name = "创建者")
    private Long creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新者")
    private Long updater;
    @Excel(name = "更新时间")
    private Date updateDate;

}