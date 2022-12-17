package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.common.dto.DishDTO;
import io.renren.common.service.DishService;
import io.renren.common.utils.Result;
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
    public Result<List<DishDTO>> listResult(DishDTO dishDTO){
        List<DishDTO> list = dishService.findDishByCategoryId(dishDTO);
        return new Result<List<DishDTO>>().ok(list);
    }

}
