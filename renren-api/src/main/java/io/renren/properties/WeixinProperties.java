package io.renren.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Yangx
 */
@Component
@ConfigurationProperties(prefix = "weixin")
@Data
public class WeixinProperties {

    private String getPhoneNumberUrl;

    private String getAccessTokenUrl;

    /**
     * 登录凭证校验请求地址
     */
    private String jscode2sessionUrl;

    /**
     * 小程序 appId
     */
    private String appid;

    /**
     * 小程序 appSecret
     */
    private String secret;


}
