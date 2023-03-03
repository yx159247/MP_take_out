package io.renren.modules.takeout.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 地址管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@ApiModel(value = "地址管理")
public class AddressBookDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "收货人")
	private String consignee;

	@ApiModelProperty(value = "性别 0 男 1 女")
	private Integer gender;

	@ApiModelProperty(value = "手机号")
	private String phone;

	@ApiModelProperty(value = "省级区划编号")
	private String provinceCode;

	@ApiModelProperty(value = "省级名称")
	private String provinceName;

	@ApiModelProperty(value = "市级区划编号")
	private String cityCode;

	@ApiModelProperty(value = "市级名称")
	private String cityName;

	@ApiModelProperty(value = "区级区划编号")
	private String districtCode;

	@ApiModelProperty(value = "区级名称")
	private String districtName;

	@ApiModelProperty(value = "详细地址")
	private String detail;

	@ApiModelProperty(value = "标签")
	private String label;

	@ApiModelProperty(value = "默认 0 否 1是")
	private Integer isDefault;

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