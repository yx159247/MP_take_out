/**
 /**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 登录表单
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "登录表单")
public class LoginDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "性别 0：男 1：女 2：保密")
    private Integer gender;

    @ApiModelProperty(value = "身份证号")
    private String idNumber;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "状态 0:禁用，1:正常")
    private Integer status;

    @ApiModelProperty(value = "微信openid")
    private String openid;

    @ApiModelProperty(value = "微信昵称")
    private String nickName;

    @ApiModelProperty(value = "创建者")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新者")
    private Long updater;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    /**
     * 获得电话号码code 前端传来的
     */
    @TableField(select = false,exist = false)
    @ApiModelProperty(value = "获得电话号码code")
    private String getPhoneCode;

    /**
     * 微信用户code 前端传来的
     */
    @TableField(select = false,exist = false)
    @ApiModelProperty(value = "微信用户code")
    private String code;

    /**
     * 微信用户敏感信息 前端传来的
     */
    @TableField(select = false,exist = false)
    @ApiModelProperty(value = "微信用户敏感信息")
    private String encryptedData;

    /**
     * 解密算法偏移量 前端传来的
     */
    @TableField(select = false,exist = false)
    @ApiModelProperty(value = "解密算法偏移量")
    private String iv;


}