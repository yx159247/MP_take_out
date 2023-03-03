package io.renren.modules.takeout.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 地址管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
@TableName("address_book")
public class AddressBookEntity extends BaseEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 用户id
     */
	private Long userId;
    /**
     * 收货人
     */
	private String consignee;
    /**
     * 性别 0 男 1 女
     */
	private Integer gender;
    /**
     * 手机号
     */
	private String phone;
    /**
     * 省级区划编号
     */
	private String provinceCode;
    /**
     * 省级名称
     */
	private String provinceName;
    /**
     * 市级区划编号
     */
	private String cityCode;
    /**
     * 市级名称
     */
	private String cityName;
    /**
     * 区级区划编号
     */
	private String districtCode;
    /**
     * 区级名称
     */
	private String districtName;
    /**
     * 详细地址
     */
	private String detail;
    /**
     * 标签
     */
	private String label;
    /**
     * 默认 0 否 1是
     */
	private Integer isDefault;

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