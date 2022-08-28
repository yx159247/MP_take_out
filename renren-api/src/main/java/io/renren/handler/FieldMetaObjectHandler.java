/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.renren.dao.TokenDao;
import io.renren.entity.TokenEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 公共字段，自动填充值
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
@Slf4j
public class FieldMetaObjectHandler implements MetaObjectHandler {
    private final static String CREATE_DATE = "createDate";
    private final static String CREATOR = "creator";
    private final static String UPDATE_DATE = "updateDate";
    private final static String UPDATER = "updater";
    public static final String USER_KEY = "userId";

    @Autowired
    private HttpServletRequest request;



    @Override
    public void insertFill(MetaObject metaObject) {
        //UserDetail user = SecurityUser.getUser();
        Long userId = (Long) request.getAttribute(USER_KEY);
        Date date = new Date();

        log.info("start insert fill ....");
        //创建者
        strictInsertFill(metaObject, CREATOR, Long.class, userId);
        //创建时间
        strictInsertFill(metaObject, CREATE_DATE, Date.class, date);

        //更新者
        strictInsertFill(metaObject, UPDATER, Long.class, userId);
        //更新时间
        strictInsertFill(metaObject, UPDATE_DATE, Date.class, date);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId = (Long) request.getAttribute(USER_KEY);
        log.info("start update  fill ....");
        log.info("userId={}",userId);
        metaObject.setValue(UPDATER,null);
        metaObject.setValue(UPDATE_DATE,null);
        //更新者
        strictUpdateFill(metaObject, UPDATER, Long.class, userId);
        //更新时间
        strictUpdateFill(metaObject, UPDATE_DATE, Date.class, new Date());
    }


}