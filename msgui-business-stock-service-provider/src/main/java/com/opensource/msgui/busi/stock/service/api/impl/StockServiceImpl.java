package com.opensource.msgui.busi.stock.service.api.impl;

import com.opensource.msgui.busi.base.service.api.impl.BusiBaseServiceImpl;
import com.opensource.msgui.busi.stock.service.api.StockService;
import com.opensource.msgui.domain.stock.Stock;
import com.opensource.msgui.repo.stock.api.RepoStockService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class StockServiceImpl extends BusiBaseServiceImpl<RepoStockService, Stock>
        implements StockService {

    @Override
    public void deduct(Long id, Integer num) {
        /**
         * 用于验证超时情况下是否能够正确回滚分布式事务
         */
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Stock originalStock = (Stock) getDomainById(id);
        Stock stock = new Stock();
        stock.setId(id);
        stock.setProductNum(originalStock.getProductNum() - num);
        updateBusiDomain(stock);
    }
}