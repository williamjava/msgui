package com.opensource.msgui.repo.stock.provider.impl;

import com.opensource.msgui.domain.stock.Stock;
import com.opensource.msgui.repo.base.service.api.impl.BaseServiceImpl;
import com.opensource.msgui.repo.stock.api.RepoStockService;
import com.opensource.msgui.repo.stock.provider.mapper.StockMapper;
import org.springframework.stereotype.Service;

@Service
public class RepoStockServiceImpl extends BaseServiceImpl<StockMapper, Stock> implements RepoStockService {

}