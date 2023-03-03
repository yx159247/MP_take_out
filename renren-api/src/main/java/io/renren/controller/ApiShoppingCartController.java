package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.common.entity.ShoppingCartEntity;
import io.renren.common.utils.Result;
import io.renren.dto.ShoppingCartDTO;
import io.renren.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信小程序购物车模块
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@RestController
@RequestMapping("mp/shoppingCart")
@Api(tags="微信小程序购物车模块")
public class ApiShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;


    @Login
    @PostMapping("/add")
    @ApiOperation("添加")
    public Result<ShoppingCartEntity> add(@RequestBody ShoppingCartDTO shoppingCartDTO){

        ShoppingCartEntity shoppingCartEntity = shoppingCartService.add2shoppingCart(shoppingCartDTO);
        return new Result().ok(shoppingCartEntity);
    }


    @Login
    @PostMapping("/sub")
    @ApiOperation("删除")
    public Result<ShoppingCartEntity> sub(@RequestBody ShoppingCartDTO shoppingCartDTO){

        ShoppingCartEntity shoppingCartEntity = shoppingCartService.subFromShoppingCart(shoppingCartDTO);
        return new Result().ok(shoppingCartEntity);
    }

    @Login
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        shoppingCartService.cleanShoppingCart();
        return new Result().ok(null);
    }


    @Login
    @GetMapping("/list")
    @ApiOperation("购物车列表")
    public Result list(){
        List<ShoppingCartEntity> shoppingCartList =  shoppingCartService.ShoppingCartList();
        return new Result().ok(shoppingCartList);
    }
}


