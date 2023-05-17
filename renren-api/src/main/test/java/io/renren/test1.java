package io.renren;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/4/24
 **/
public class test1 {
    @Test
    public void test(){
        int year = DateUtil.year(new Date());
        int month = DateUtil.month(new Date());
        System.out.println(year);
        System.out.println(month + 1);
    }

}
