package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 手机号登录表单
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/2/1
 **/
@Data
@ApiModel(value = "手机号登录表单")
public class PhoneLoginDto {
    @ApiModelProperty(value = "手机号")
    @NotBlank(message="手机号不能为空")
    private String phone;



    @ApiModelProperty(value = "验证码")
    @NotBlank(message="验证码不能为空")
    private String code;
}
