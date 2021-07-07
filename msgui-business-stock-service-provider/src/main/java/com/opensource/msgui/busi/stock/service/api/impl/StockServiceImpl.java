package com.opensource.msgui.busi.stock.service.api.impl;

import com.opensource.msgui.busi.base.service.api.impl.BusiBaseServiceImpl;
import com.opensource.msgui.busi.stock.service.api.StockService;
import com.opensource.msgui.domain.stock.Stock;
import com.opensource.msgui.repo.stock.api.RepoStockService;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl extends BusiBaseServiceImpl<RepoStockService, Stock>
        implements StockService {

}
