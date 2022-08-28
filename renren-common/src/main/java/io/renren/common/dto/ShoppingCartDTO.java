package io.renren.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 购物车
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@ApiModel(value = "购物车")
public class ShoppingCartDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "图片")
	private String image;

	@ApiModelProperty(value = "主键")
	private Long userId;

	@ApiModelProperty(value = "菜品id")
	private Long dishId;

	@ApiModelProperty(value = "套餐id")
	private Long setmealId;

	@ApiModelProperty(value = "口味")
	private String dishFlavor;

	@ApiModelProperty(value = "数量")
	private Integer number;

	@ApiModelProperty(value = "金额")
	private BigDecimal amount;

	@ApiModelProperty(value = "创建者")
	private Long creator;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "更新者")
	private Long updater;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;


}