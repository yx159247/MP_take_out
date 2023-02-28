package io.renren.dto;

import lombok.Data;

/**
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/2/6
 **/
@Data
public class UpdateUserInfoDto extends PhoneLoginDto{
    private String nickName;
    private Integer gender;
    private String avatarUrl;
}
