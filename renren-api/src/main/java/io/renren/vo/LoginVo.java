package io.renren.vo;

import lombok.Data;

/**
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/1/31
 **/
@Data
public class LoginVo {
    private String token;
    private Long userId;
    private String phone;
    private String nickName;
    private String avatarUrl;

}
