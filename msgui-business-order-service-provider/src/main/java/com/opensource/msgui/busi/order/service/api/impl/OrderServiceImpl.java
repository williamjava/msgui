package com.opensource.msgui.busi.order.service.api.impl;

import com.opensource.msgui.busi.base.service.api.impl.BusiBaseServiceImpl;
import com.opensource.msgui.busi.order.service.api.OrderService;
import com.opensource.msgui.busi.stock.service.api.StockService;
import com.opensource.msgui.domain.order.Order;
import com.opensource.msgui.repo.order.api.RepoOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl extends BusiBaseServiceImpl<RepoOrderService, Order>
        implements OrderService {
    @Resource
    private StockService stockService;

    @SneakyThrows
    @Override
    @GlobalTransactional(name = "fsp-create-order", rollbackFor = Exception.class)
    public void create(Order order) {
        try {
            log.info("开始创建订单....");
            //1.创建订单
            addDomain(order);

            //2.扣减库存
            log.info("开始扣减库存...");
            stockService.deduct(order.getStockId(), order.getStockNum());

            log.info("创建订单结束...");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
