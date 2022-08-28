package io.renren.modules.takeout.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 套餐菜品关系
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
public class SetmealDishExcel {
    @Excel(name = "主键")
    private Long id;
    @Excel(name = "套餐id ")
    private String setmealId;
    @Excel(name = "菜品id")
    private String dishId;
    @Excel(name = "菜品名称 （冗余字段）")
    private String name;
    @Excel(name = "菜品原价（冗余字段）")
    private BigDecimal price;
    @Excel(name = "份数")
    private Integer copies;
    @Excel(name = "排序")
    private Integer sort;
    @Excel(name = "创建人")
    private Long creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "修改人")
    private Long updater;
    @Excel(name = "更新时间")
    private Date updateDate;
    @Excel(name = "是否删除")
    private Integer isDeleted;

}