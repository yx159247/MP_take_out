package io.renren.modules.takeout.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailInfoVo {
    /**
     * 名字
     */
    private String name;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 金额
     */
    private BigDecimal amount;
}
