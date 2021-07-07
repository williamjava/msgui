package com.opensource.msgui.manager.pay.api.alipay.impl;

import cn.hutool.core.map.MapUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.opensource.msgui.commons.utils.MapperUtils;
import com.opensource.msgui.manager.pay.alipay.AlipayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlipayServiceImpl implements AlipayService {
    private static final Logger log = LoggerFactory.getLogger(AlipayServiceImpl.class);

    @Autowired
    private AlipayClient alipayClient;

    @Value("${alipay.notify}")
    private String notify;

    @Value("${alipay.rsa_pub_key}")
    private String ALIPAY_PUBLIC_KEY;

    @Override
    public Map addPrePayOrder(String body, String orderNo, String totalFee) throws Exception {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        TradePrecreateDto dto = new TradePrecreateDto();
        dto.setQr_code_timeout_express("90m");
        dto.setOut_trade_no(orderNo);
        dto.setSubject(body);
        dto.setTotal_amount(totalFee);
        request.setBizContent(MapperUtils.obj2json(dto));
        request.setNotifyUrl(this.notify);
        AlipayTradePrecreateResponse response = (AlipayTradePrecreateResponse) this.alipayClient.execute((AlipayRequest) request);
        if (response.isSuccess()) {
            HashMap<Object, Object> map = new HashMap<>();
            map.put("orderNo", response.getOutTradeNo());
            map.put("qr_code", response.getQrCode());
            log.info("成功");
            return map;
        }
        log.error(MapperUtils.obj2json(response));
        return null;
    }

    static class TradePrecreateDto implements Serializable {
        private String out_trade_no;

        private String total_amount;

        private String subject;

        private String qr_code_timeout_express;

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public void setQr_code_timeout_express(String qr_code_timeout_express) {
            this.qr_code_timeout_express = qr_code_timeout_express;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof TradePrecreateDto)) {
                return false;
            }
            TradePrecreateDto other = (TradePrecreateDto) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$out_trade_no = getOut_trade_no(), other$out_trade_no = other.getOut_trade_no();
            if ((this$out_trade_no == null) ? (other$out_trade_no != null) : !this$out_trade_no.equals(other$out_trade_no)) {
                return false;
            }
            Object this$total_amount = getTotal_amount(), other$total_amount = other.getTotal_amount();
            if ((this$total_amount == null) ? (other$total_amount != null) : !this$total_amount.equals(other$total_amount)) {
                return false;
            }
            Object this$subject = getSubject(), other$subject = other.getSubject();
            if ((this$subject == null) ? (other$subject != null) : !this$subject.equals(other$subject)) {
                return false;
            }
            Object this$qr_code_timeout_express = getQr_code_timeout_express(), other$qr_code_timeout_express = other.getQr_code_timeout_express();
            return !((this$qr_code_timeout_express == null) ? (other$qr_code_timeout_express != null) : !this$qr_code_timeout_express.equals(other$qr_code_timeout_express));
        }

        protected boolean canEqual(Object other) {
            return other instanceof TradePrecreateDto;
        }

        @Override
        public int hashCode() {
            int result = 1;
            Object $out_trade_no = getOut_trade_no();
            result = result * 59 + (($out_trade_no == null) ? 43 : $out_trade_no.hashCode());
            Object $total_amount = getTotal_amount();
            result = result * 59 + (($total_amount == null) ? 43 : $total_amount.hashCode());
            Object $subject = getSubject();
            result = result * 59 + (($subject == null) ? 43 : $subject.hashCode());
            Object $qr_code_timeout_express = getQr_code_timeout_express();
            return result * 59 + (($qr_code_timeout_express == null) ? 43 : $qr_code_timeout_express.hashCode());
        }

        @Override
        public String toString() {
            return "AlipayServiceImpl.TradePrecreateDto(out_trade_no=" + getOut_trade_no() + ", total_amount=" + getTotal_amount() + ", subject=" + getSubject() + ", qr_code_timeout_express=" + getQr_code_timeout_express() + ")";
        }

        public String getOut_trade_no() {
            return this.out_trade_no;
        }

        public String getTotal_amount() {
            return this.total_amount;
        }

        public String getSubject() {
            return this.subject;
        }

        public String getQr_code_timeout_express() {
            return this.qr_code_timeout_express;
        }
    }

    @Override
    public boolean paramsSign(Map requestParams) throws Exception {
        Map<String, String> params = new HashMap<>();
        boolean empty = MapUtil.isEmpty(requestParams);
        if (empty) {
            return false;
        }
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? (valueStr + values[i]) : (valueStr + values[i] + ",");
            }
            log.info("valueStr:==>" + valueStr);
            params.put(name, valueStr);
        }
        return AlipaySignature.rsaCheckV1(params, this.ALIPAY_PUBLIC_KEY, "UTF-8", "RSA2");
    }
}
