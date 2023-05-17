package io.renren.modules.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.renren.common.utils.UserUtils;
import io.renren.modules.takeout.dao.AddressBookDao;
import io.renren.modules.takeout.dto.AddressBookDTO;
import io.renren.modules.takeout.entity.AddressBookEntity;
import io.renren.modules.takeout.service.AddressBookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 地址管理
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@Service
public class AddressBookServiceImpl extends CrudServiceImpl<AddressBookDao, AddressBookEntity, AddressBookDTO> implements AddressBookService {

    @Autowired
    private AddressBookDao addressBookDao;
    @Autowired
    private UserUtils userUtils;

    @Override
    public QueryWrapper<AddressBookEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<AddressBookEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


}