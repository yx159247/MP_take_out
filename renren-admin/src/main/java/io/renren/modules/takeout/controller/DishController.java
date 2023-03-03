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
import io.renren.modules.takeout.dto.DishDTO;
import io.renren.modules.takeout.excel.DishExcel;
import io.renren.modules.takeout.service.DishService;
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
 * 菜品管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@RestController
@RequestMapping("takeout/dish")
@Api(tags="菜品管理")
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("takeout:dish:page")
    public Result<PageData<DishDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<DishDTO> page = dishService.page(params);

        return new Result<PageData<DishDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("takeout:dish:info")
    public Result<DishDTO> get(@PathVariable("id") Long id){
        //DishDTO data = dishService.get(id);

        DishDTO data = dishService.getByWithFlavor(id);
        if (data != null){
            return new Result<DishDTO>().ok(data);
        }
        return new Result<DishDTO>().error("没有查询到对应菜品信息");
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("takeout:dish:save")
    public Result<String> save(@RequestBody DishDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        //dishService.save(dto);
        dishService.saveWithFlavor(dto);

        return new Result<String>().ok("新增菜品成功");
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("takeout:dish:update")
    public Result<String> update(@RequestBody DishDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        //dishService.update(dto);
        dishService.updateWithFlavor(dto);

        return new Result<String>().ok("菜品信息修改成功");
    }

    @PutMapping("/updateStatus")
    @ApiOperation("批量修改")
    @LogOperation("批量修改")
    @RequiresPermissions("takeout:dish:update")
    public Result updateStatus(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        //dishService.update(dto);
        dishService.updateStatus(ids);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("takeout:dish:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        dishService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("takeout:dish:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<DishDTO> list = dishService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, DishExcel.class);
    }

    @GetMapping("list")
    @ApiOperation("查看菜品")
    @RequiresPermissions("takeout:dish:list")
    public Result<List<DishDTO>> listResult(DishDTO dishDTO){
        List<DishDTO> list = dishService.findDishByCategoryId(dishDTO);
        return new Result<List<DishDTO>>().ok(list);
    }

}