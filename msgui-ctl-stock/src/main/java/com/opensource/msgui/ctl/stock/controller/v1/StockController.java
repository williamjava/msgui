package com.opensource.msgui.ctl.stock.controller.v1;

import com.opensource.msgui.busi.stock.service.api.StockService;
import com.opensource.msgui.domain.stock.Stock;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whj
 *
 * 库存管理控制器
 */
@RestController
@RequestMapping("/api/stock/v1")
public class StockController extends BaseController<StockService, Stock> {

}

    
