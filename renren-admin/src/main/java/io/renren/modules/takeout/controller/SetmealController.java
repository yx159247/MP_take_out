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
import io.renren.modules.takeout.dto.SetmealDTO;
import io.renren.modules.takeout.excel.SetmealExcel;
import io.renren.modules.takeout.service.SetmealService;
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
 * 套餐
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@RestController
@RequestMapping("takeout/setmeal")
@Api(tags="套餐")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("takeout:setmeal:page")
    public Result<PageData<SetmealDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SetmealDTO> page = setmealService.page(params);

        return new Result<PageData<SetmealDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("takeout:setmeal:info")
    public Result<SetmealDTO> get(@PathVariable("id") Long id){

        SetmealDTO setmealDTO = setmealService.getByWithDish(id);
        if (setmealDTO != null){
            return new Result<SetmealDTO>().ok(setmealDTO);
        }
        //SetmealDTO data = setmealService.get(id);

        return new Result<SetmealDTO>().error("没有查询到对应套餐信息");
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("takeout:setmeal:save")
    public Result save(@RequestBody SetmealDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        setmealService.saveWithDishes(dto);

        return new Result();
    }

    @PutMapping("/updateStatus")
    @ApiOperation("批量修改")
    @LogOperation("批量修改")
    @RequiresPermissions("takeout:setmeal:update")
    public Result updateStatus(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        //dishService.update(dto);
        setmealService.updateStatus(ids);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("takeout:setmeal:update")
    public Result update(@RequestBody SetmealDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        setmealService.updateWithDishes(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("takeout:setmeal:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        setmealService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("takeout:setmeal:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SetmealDTO> list = setmealService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, SetmealExcel.class);
    }

}