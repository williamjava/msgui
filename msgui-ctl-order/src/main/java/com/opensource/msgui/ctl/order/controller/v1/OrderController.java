package com.opensource.msgui.ctl.order.controller.v1;

import com.opensource.msgui.busi.order.service.api.OrderService;
import com.opensource.msgui.commons.response.ResponseResult;
import com.opensource.msgui.domain.order.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    /**
     * 下单
     * @return
     */
    @PostMapping("create")
    public ResponseResult initStock(@RequestBody Order order) {
        try {
            this.busiService.create(order);
            return ResponseResult.success("下单成功");
        } catch (Exception e) {
            return ResponseResult.failure("下单失败");
        }
    }
}