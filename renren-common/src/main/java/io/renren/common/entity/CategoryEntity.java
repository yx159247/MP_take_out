package io.renren.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 菜品及套餐分类
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@TableName("category")
public class CategoryEntity extends BaseEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 类型   1 菜品分类 2 套餐分类
     */
	private Integer type;
    /**
     * 分类名称
     */
	private String name;
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
}