package io.renren.modules.takeout.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
public class UserExcel {
    @Excel(name = "id")
    private Long id;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "手机号")
    private String phone;
    @Excel(name = "性别 0：男 1：女 2：保密")
    private Integer gender;
    @Excel(name = "身份证号")
    private String idNumber;
    @Excel(name = "头像")
    private String avatarUrl;
    @Excel(name = "状态 0:禁用，1:正常")
    private Integer status;
    @Excel(name = "微信openid")
    private String openid;
    @Excel(name = "微信昵称")
    private String nickName;
    @Excel(name = "创建者")
    private Long creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "更新者")
    private Long updater;
    @Excel(name = "更新时间")
    private Date updateDate;

}