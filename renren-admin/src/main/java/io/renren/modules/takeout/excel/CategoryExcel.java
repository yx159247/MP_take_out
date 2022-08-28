package io.renren.modules.takeout.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 菜品及套餐分类
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Data
public class CategoryExcel {
    @Excel(name = "主键")
    private Long id;
    @Excel(name = "类型   1 菜品分类 2 套餐分类")
    private Integer type;
    @Excel(name = "分类名称")
    private String name;
    @Excel(name = "顺序")
    private Integer sort;
    @Excel(name = "创建人")
    private Long creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "修改人")
    private Long updater;
    @Excel(name = "更新时间")
    private Date updateDate;

}