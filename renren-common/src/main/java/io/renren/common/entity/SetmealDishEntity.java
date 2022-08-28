package io.renren.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 套餐菜品关系
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@TableName("setmeal_dish")
public class SetmealDishEntity extends BaseEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 套餐id 
     */
	private String setmealId;
    /**
     * 菜品id
     */
	private String dishId;
    /**
     * 菜品名称 （冗余字段）
     */
	private String name;
    /**
     * 菜品原价（冗余字段）
     */
	private BigDecimal price;
    /**
     * 份数
     */
	private Integer copies;
    /**
     * 排序
     */
	private Integer sort;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
    /**
     * 是否删除
     */
	private Integer isDeleted;
}