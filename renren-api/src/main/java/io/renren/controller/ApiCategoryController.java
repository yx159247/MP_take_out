package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.common.dto.CategoryDTO;
import io.renren.common.entity.CategoryEntity;
import io.renren.common.service.CategoryService;
import io.renren.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 微信小程序分类模块
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@RestController
@RequestMapping("mp/category")
@Api(tags="微信小程序分类模块")
public class ApiCategoryController {
    @Autowired
    private CategoryService categoryService;

    @Login
    @GetMapping("list")
    @ApiOperation("分类")
    public Result<List<CategoryDTO>> list(CategoryDTO categoryDTO){

        List<CategoryDTO> categoryDTOList = categoryService.findCategoryByQuery(categoryDTO);
        return new Result().ok(categoryDTOList);
    }

}
