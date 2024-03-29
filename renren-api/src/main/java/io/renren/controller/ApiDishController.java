package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.dto.DishDTO;
import io.renren.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 微信小程序菜品模块
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@RestController
@RequestMapping("mp/dish")
@Api(tags="微信小程序菜品模块")
public class ApiDishController {
    @Autowired
    private DishService dishService;



    @GetMapping("list")
    @ApiOperation("菜品")
    public Result listResult(DishDTO dishDTO){
        List<DishDTO> list = dishService.findDishByCategoryId(dishDTO);
        if (list.size() == 0){
            return new Result<>().error("菜品信息不存在");
        }
        return new Result<List<DishDTO>>().ok(list);
    }

}
