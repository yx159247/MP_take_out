package io.renren.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.renren.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 套餐
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@TableName("setmeal")
public class SetmealEntity extends BaseEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 菜品分类id
     */
	private Long categoryId;
    /**
     * 套餐名称
     */
	private String name;
    /**
     * 套餐价格
     */
	private BigDecimal price;
    /**
     * 状态 0:停用 1:启用
     */
	private Integer status;
    /**
     * 编码
     */
	private String code;
    /**
     * 描述信息
     */
	private String description;
    /**
     * 图片
     */
	private String image;

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