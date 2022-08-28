package io.renren.common.dto;

import io.renren.common.entity.SetmealDishEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
import java.util.List;

/**
 * 套餐
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@ApiModel(value = "套餐")
public class SetmealDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "菜品分类id")
	private Long categoryId;

	@ApiModelProperty(value = "套餐名称")
	private String name;

	@ApiModelProperty(value = "套餐价格")
	private BigDecimal price;

	@ApiModelProperty(value = "状态 0:停用 1:启用")
	private Integer status;

	@ApiModelProperty(value = "编码")
	private String code;

	@ApiModelProperty(value = "描述信息")
	private String description;

	@ApiModelProperty(value = "图片")
	private String image;

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

	/**
	 * 套餐菜品列表
	 */
	private List<SetmealDishEntity> setmealDishes;

	/**
	 * 分类名称
	 */
	private String categoryName;

}