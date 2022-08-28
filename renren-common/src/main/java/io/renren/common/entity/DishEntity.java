package io.renren.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 菜品管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@TableName("dish")
public class DishEntity extends BaseEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 菜品名称
     */
	private String name;
    /**
     * 菜品分类id
     */
	private Long categoryId;
    /**
     * 菜品价格
     */
	private BigDecimal price;
    /**
     * 商品码
     */
	private String code;
    /**
     * 图片
     */
	private String image;
    /**
     * 描述信息
     */
	private String description;
    /**
     * 0 停售 1 起售
     */
	private Integer status;
    /**
     * 顺序
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