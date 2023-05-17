package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.common.utils.Result;
import io.renren.dto.SetmealDTO;
import io.renren.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 微信小程序套餐模块
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@RestController
@RequestMapping("mp/setmeal")
@Api(tags="微信小程序套餐模块")
public class ApiSetmealController {
    @Autowired
    private SetmealService setmealService;



    @GetMapping("/{id}")
    @ApiOperation("套餐信息")
    public Result<SetmealDTO> get(@PathVariable("id") Long id){

        SetmealDTO setmealDTO = setmealService.getByWithDish(id);
        if (setmealDTO != null){
            return new Result<SetmealDTO>().ok(setmealDTO);
        }
        //SetmealDTO data = setmealService.get(id);

        return new Result<SetmealDTO>().error("没有查询到对应套餐信息");
    }



    @GetMapping("/list")
    @ApiOperation("套餐列表")
    public Result<List<SetmealDTO>> list(SetmealDTO setmealDTO){
        List<SetmealDTO> setmealEntityList = setmealService.findSetmealByCategoryId(setmealDTO);
        return new Result().ok(setmealEntityList);
    }

}
