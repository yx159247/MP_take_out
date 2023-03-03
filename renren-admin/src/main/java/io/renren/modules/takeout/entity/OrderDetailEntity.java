package io.renren.modules.takeout.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@TableName("order_detail")
public class OrderDetailEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 名字
     */
	private String name;
    /**
     * 图片
     */
	private String image;
    /**
     * 订单id
     */
	private Long orderId;
    /**
     * 菜品id
     */
	private Long dishId;
    /**
     * 套餐id
     */
	private Long setmealId;
    /**
     * 口味
     */
	private String dishFlavor;
    /**
     * 数量
     */
	private Integer number;
    /**
     * 金额
     */
	private BigDecimal amount;
}