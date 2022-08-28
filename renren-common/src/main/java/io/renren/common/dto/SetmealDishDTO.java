package io.renren.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 套餐菜品关系
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@ApiModel(value = "套餐菜品关系")
public class SetmealDishDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "套餐id ")
	private String setmealId;

	@ApiModelProperty(value = "菜品id")
	private String dishId;

	@ApiModelProperty(value = "菜品名称 （冗余字段）")
	private String name;

	@ApiModelProperty(value = "菜品原价（冗余字段）")
	private BigDecimal price;

	@ApiModelProperty(value = "份数")
	private Integer copies;

	@ApiModelProperty(value = "排序")
	private Integer sort;

	@ApiModelProperty(value = "创建人")
	private Long creator;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "修改人")
	private Long updater;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;

	@ApiModelProperty(value = "是否删除")
	private Integer isDeleted;


}