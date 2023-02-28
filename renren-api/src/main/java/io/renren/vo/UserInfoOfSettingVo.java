package io.renren.vo;

import lombok.Data;

/**
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/2/6
 **/
@Data
public class UserInfoOfSettingVo {
    private String avatarUrl;
    private String nickName;

    private Integer gender;

    private String phone;
}
