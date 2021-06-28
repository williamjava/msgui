package com.opensource.msgui.repo.base.mp.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.opensource.msgui.commons.utils.LocalDateTimeUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author whj
 *
 * mybatis设置工具属性
 */
@Component
public class MsguiMetaObjectHandler implements MetaObjectHandler {
    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String DELETED = "deleted";

    public MsguiMetaObjectHandler() {
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        boolean hasCreateTime = metaObject.hasSetter(CREATE_TIME);
        boolean hasUpdateTime = metaObject.hasSetter(UPDATE_TIME);
        boolean hasDeleted = metaObject.hasSetter(DELETED);
        if (hasCreateTime) {
            this.strictInsertFill(metaObject, CREATE_TIME, String.class, LocalDateTimeUtil.getDateNowSec());
        }

        if (hasUpdateTime) {
            this.strictInsertFill(metaObject, UPDATE_TIME, String.class, LocalDateTimeUtil.getDateNowSec());
        }

        if (hasDeleted) {
            this.strictInsertFill(metaObject, DELETED, Boolean.class, false);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object val = this.getFieldValByName(UPDATE_TIME, metaObject);
        if (val == null) {
            this.strictUpdateFill(metaObject, UPDATE_TIME, String.class, LocalDateTimeUtil.getDateNowSec());
        }

    }
}
