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
import io.renren.common.dto.OrderDetailDTO;
import io.renren.modules.takeout.excel.OrderDetailExcel;
import io.renren.common.service.OrderDetailService;
import io.renren.common.vo.OrderDetailInfoVo;
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
 * 订单明细表
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@RestController
@RequestMapping("takeout/orderdetail")
@Api(tags="订单明细表")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("takeout:orderdetail:page")
    public Result<PageData<OrderDetailDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<OrderDetailDTO> page = orderDetailService.page(params);

        return new Result<PageData<OrderDetailDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("takeout:orderdetail:info")
    public Result<OrderDetailDTO> get(@PathVariable("id") Long id){
        OrderDetailDTO data = orderDetailService.get(id);

        return new Result<OrderDetailDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("takeout:orderdetail:save")
    public Result save(@RequestBody OrderDetailDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        orderDetailService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("takeout:orderdetail:update")
    public Result update(@RequestBody OrderDetailDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        orderDetailService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("takeout:orderdetail:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        orderDetailService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("takeout:orderdetail:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<OrderDetailDTO> list = orderDetailService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, list, OrderDetailExcel.class);
    }

    @GetMapping("/detailInfo/{id}")
    @ApiOperation("订单信息")
    @RequiresPermissions("takeout:orderdetail:detailInfo")
    public Result<List<OrderDetailInfoVo>> getById(@PathVariable("id") Long id){

        List<OrderDetailInfoVo> voList = orderDetailService.getByOrderId(id);

        return new Result<List<OrderDetailInfoVo>>().ok(voList);

    }



}