package io.renren.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 菜品口味关系表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@TableName("dish_flavor")
public class DishFlavorEntity extends BaseEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 菜品
     */
	private Long dishId;
    /**
     * 口味名称
     */
	private String name;
    /**
     * 口味数据list
     */
	private String value;

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