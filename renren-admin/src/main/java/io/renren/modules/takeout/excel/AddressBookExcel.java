package io.renren.modules.takeout.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 地址管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
public class AddressBookExcel {
    @Excel(name = "主键")
    private Long id;
    @Excel(name = "用户id")
    private Long userId;
    @Excel(name = "收货人")
    private String consignee;
    @Excel(name = "性别 0 男 1 女")
    private Integer gender;
    @Excel(name = "手机号")
    private String phone;
    @Excel(name = "省级区划编号")
    private String provinceCode;
    @Excel(name = "省级名称")
    private String provinceName;
    @Excel(name = "市级区划编号")
    private String cityCode;
    @Excel(name = "市级名称")
    private String cityName;
    @Excel(name = "区级区划编号")
    private String districtCode;
    @Excel(name = "区级名称")
    private String districtName;
    @Excel(name = "详细地址")
    private String detail;
    @Excel(name = "标签")
    private String label;
    @Excel(name = "默认 0 否 1是")
    private Integer isDefault;
    @Excel(name = "创建人")
    private Long creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "修改人")
    private Long updater;
    @Excel(name = "更新时间")
    private Date updateDate;
    @Excel(name = "是否删除")
    private Integer isDeleted;

}