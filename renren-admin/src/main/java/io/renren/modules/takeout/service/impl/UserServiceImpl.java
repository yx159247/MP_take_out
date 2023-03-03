package io.renren.modules.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.entity.UserEntity;
import io.renren.modules.takeout.dao.UserDao;
import io.renren.modules.takeout.dto.UserDTO;
import io.renren.modules.takeout.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户信息
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class UserServiceImpl extends CrudServiceImpl<UserDao, UserEntity, UserDTO> implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public QueryWrapper<UserEntity> getWrapper(Map<String, Object> params){


        String id = (String)params.get("id");

        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(id), "phone", id);

        return wrapper;
    }


    @Override
    public long userCount() {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        long count = userDao.selectCount(queryWrapper);
        return count;
    }
}