package com.opensource.msgui.ctl.order.controller.v1;

import com.opensource.msgui.busi.base.service.api.BusiBaseService;
import com.opensource.msgui.ctl.base.GlobalController;
import com.opensource.msgui.domain.base.Domain;

public class BaseController<M extends BusiBaseService, T extends Domain> extends GlobalController<M,T> {

    @Override
    protected Domain getUser() {
        return null;
    }
}
