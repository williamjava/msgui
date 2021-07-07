package com.opensource.msgui.ctl.user.controller.v1.notify;

import com.opensource.msgui.commons.utils.MapperUtils;
import com.opensource.msgui.manager.pay.alipay.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author whj
 *
 * 支付宝支付回调方法模板
 */
@Slf4j
@RestController
@RequestMapping("/notify")
@EnableAsync
public class AlipayNotifyController {
    @Resource
    private AlipayService alipayService;

    @PostMapping("/alipay")
    public String aliPayNotify(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        try {
            Map requestParams = request.getParameterMap();
            log.info("notify=>" + MapperUtils.mapToJson(requestParams));
            boolean verify_result = alipayService.paramsSign(requestParams);
            log.info(String.valueOf(verify_result));
            //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            //商户订单号

            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号

            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            //验证成功
            if (verify_result) {
                log.info("验签成功");

                if (trade_status.equals("TRADE_FINISHED")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                    //如果有做过处理，不执行商户的业务程序

                    log.info(trade_status + "===>business to do....");
                } else if (trade_status.equals("TRADE_SUCCESS")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                    //如果有做过处理，不执行商户的业务程序
                    log.info(trade_status + "===>business to do....");
                }
            } else {
                log.info("验签失败");
                return "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

        return "success";
    }
}
