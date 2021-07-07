package com.opensource.msgui.busi.order.service.api.impl;

import com.opensource.msgui.busi.base.service.api.impl.BusiBaseServiceImpl;
import com.opensource.msgui.busi.order.service.api.OrderService;
import com.opensource.msgui.domain.order.Order;
import com.opensource.msgui.repo.order.api.RepoOrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends BusiBaseServiceImpl<RepoOrderService, Order>
        implements OrderService {

}
