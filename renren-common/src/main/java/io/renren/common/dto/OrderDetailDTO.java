package io.renren.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 订单明细表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@ApiModel(value = "订单明细表")
public class OrderDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "名字")
	private String name;

	@ApiModelProperty(value = "图片")
	private String image;

	@ApiModelProperty(value = "订单id")
	private Long orderId;

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


}