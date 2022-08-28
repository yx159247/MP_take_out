package io.renren.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
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
@TableName("shopping_cart")
public class ShoppingCartEntity extends BaseEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 名称
     */
	private String name;
    /**
     * 图片
     */
	private String image;
    /**
     * 主键
     */
	private Long userId;
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

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}