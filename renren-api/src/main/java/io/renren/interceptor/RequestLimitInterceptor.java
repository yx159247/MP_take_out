package io.renren.interceptor;

import io.renren.annotation.AccessLimit;
import io.renren.common.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/4/26
 **/
@Component
@Slf4j
public class RequestLimitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//       1. 判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {
//       2. 取出当前方法的对象
            HandlerMethod handler1 = (HandlerMethod) handler;
//       3. 获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = handler1.getMethodAnnotation(AccessLimit.class);
//            3.1 : 不包含此注解,则不进行操作
            if (accessLimit != null) {
//                3.2 ： 判断请求是否受限制
                if (isLimit(request, accessLimit)) {
                    render(response, "{\"code\":\"10030\",\"msg\":\"请求过快\"}");

                    return false;
                }
            }
        }
        return true;
    }
    //判断请求是否受限
    public boolean isLimit(HttpServletRequest request, AccessLimit accessLimit) {
        String ip = request.getRemoteAddr(); // 获取客户端IP地址
        String ipLimitKey = "request_limit_" + ip;
        //String tokenLimitKey = request.getHeader("token");
        // 从缓存中获取，当前这个请求访问了几次

        Integer redisCount = (Integer) redisUtils.get(ipLimitKey);
        if (redisCount == null) {
            //初始 次数
            redisUtils.set(ipLimitKey, 1, accessLimit.seconds());
            log.info("当前ip初次请求，写入Redis");
        } else {
            log.info("当前ip请求次数:{}", redisCount);
            if (redisCount >= accessLimit.maxCount()) {
                return true;
            }
            // 次数自增
            redisUtils.increment(ipLimitKey);
        }
        return false;
    }

    private void render(HttpServletResponse response, String cm) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(cm.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
