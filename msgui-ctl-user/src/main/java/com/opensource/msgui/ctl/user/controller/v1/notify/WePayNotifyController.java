package com.opensource.msgui.ctl.user.controller.v1.notify;

import com.opensource.msgui.commons.utils.MapperUtils;
import com.opensource.msgui.manager.pay.wechat.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;


/**
 * @author whj
 *
 * 微信支付回调方法模板
 */
@Slf4j
@RestController
@RequestMapping("/notify")
@EnableAsync
public class WePayNotifyController {
//    @Value("${pay.we.key}")
//    private String key;

    @PostMapping("/wxpay")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) {
        String success = "<xml>"
                + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[OK]]></return_msg>"
                + "</xml> ";

        try {
            InputStream inputStream = request.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            inputStreamReader.close();
            inputStream.close();
            String xml = sb.toString();
            log.info(xml);
            boolean isValid = WXPayUtil.isSignatureValid(xml, null);
            if (isValid) {
                log.info("验签成功");

                //业务代码
                Map<String, String> parasMap = WXPayUtil.xmlToMap(xml);
                log.info(MapperUtils.mapToJson(parasMap));
                //业务订单号
                String outTradeNo = parasMap.get("out_trade_no");
                //微信支付状态码
                String result_code = parasMap.get("result_code");
                //微信支付订单流水号
                String tradeNo = parasMap.get("transaction_id");

                //支付成功，更新订单状态
                if ("SUCCESS".equals(result_code)) {
                    //业务代码
                    log.info("business to do....");
                }
            } else {
                log.info("验签失败");
                return "fail";
            }

            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
}
