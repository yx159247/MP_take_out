package io.renren.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取当前登录用户ID
 * @author Yangx
 */
@Component
public class UserUtils {
    @Autowired
    private HttpServletRequest request ;

    public Long getUserId(){

        return Long.parseLong(request.getAttribute("userId").toString());
    }
}
