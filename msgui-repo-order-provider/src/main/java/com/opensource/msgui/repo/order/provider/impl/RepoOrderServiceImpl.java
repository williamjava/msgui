package com.opensource.msgui.repo.order.provider.impl;

import com.opensource.msgui.domain.order.Order;
import com.opensource.msgui.repo.base.service.api.impl.BaseServiceImpl;
import com.opensource.msgui.repo.order.api.RepoOrderService;
import com.opensource.msgui.repo.order.provider.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class RepoOrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements RepoOrderService {

}