package com.opensource.msgui.ctl.order.controller.v1;

import com.opensource.msgui.busi.order.service.api.OrderService;
import com.opensource.msgui.domain.order.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whj
 *
 * 订单管理控制器
 */
@RestController
@RequestMapping("/api/order/v1")
public class OrderController extends BaseController<OrderService, Order> {

}