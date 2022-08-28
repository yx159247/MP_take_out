package io.renren.modules.takeout.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 菜品口味关系表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
public class DishFlavorExcel {
    @Excel(name = "主键")
    private Long id;
    @Excel(name = "菜品")
    private Long dishId;
    @Excel(name = "口味名称")
    private String name;
    @Excel(name = "口味数据list")
    private String value;
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