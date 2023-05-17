package io.renren.annotation;

import java.lang.annotation.*;

/**
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/4/26
 **/
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    /**
     * 在  seconds 秒内 , 最大只能请求 maxCount 次
     *
     * @return
     */
    //    秒
    int seconds() default 1;

    //    最大数量
    int maxCount() default 1;
}
