package io.renren.modules.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.entity.CategoryEntity;
import io.renren.modules.takeout.dao.CategoryDao;
import io.renren.modules.takeout.dto.CategoryDTO;
import io.renren.modules.takeout.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜品及套餐分类
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class CategoryServiceImpl extends CrudServiceImpl<CategoryDao, CategoryEntity, CategoryDTO> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    @Override
    public QueryWrapper<CategoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<CategoryDTO> findCategoryByQuery(CategoryDTO categoryDTO) {
        LambdaQueryWrapper<CategoryEntity> queryWrapper  = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryDTO.getType() != null,CategoryEntity::getType,categoryDTO.getType());
        queryWrapper.orderByDesc(CategoryEntity::getSort).orderByDesc(CategoryEntity::getUpdateDate);
        List<CategoryDTO> categoryDTOList = categoryDao.selectList(queryWrapper).stream().map(categoryEntity -> {
            CategoryDTO dto = new CategoryDTO();
            BeanUtils.copyProperties(categoryEntity, dto);
            return dto;
        }).collect(Collectors.toList());

        return categoryDTOList;
    }
}