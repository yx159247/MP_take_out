package io.renren.modules.takeout.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.takeout.dto.DishFlavorDTO;
import io.renren.modules.takeout.excel.DishFlavorExcel;
import io.renren.modules.takeout.service.DishFlavorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 菜品口味关系表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@RestController
@RequestMapping("takeout/dishflavor")
@Api(tags="菜品口味关系表")
public class DishFlavorController {
    @Autowired
    private DishFlavorService dishFlavorService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("takeout:dishflavor:page")
    public Result<PageData<DishFlavorDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<DishFlavorDTO> page = dishFlavorService.page(params);

        return new Result<PageData<DishFlavorDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("takeout:dishflavor:info")
    public Result<DishFlavorDTO> get(@PathVariable("id") Long id){
        DishFlavorDTO data = dishFlavorService.get(id);

        return new Result<DishFlavorDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("takeout:dishflavor:save")
    public Result save(@RequestBody DishFlavorDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        dishFlavorService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("takeout:dishflavor:update")
    public Result update(@RequestBody DishFlavorDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        dishFlavorService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("takeout:dishflavor:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        dishFlavorService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("takeout:dishflavor:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<DishFlavorDTO> list = dishFlavorService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, DishFlavorExcel.class);
    }

}