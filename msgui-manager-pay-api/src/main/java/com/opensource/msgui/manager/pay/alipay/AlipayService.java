package com.opensource.msgui.manager.pay.alipay;

import java.util.Map;

public interface AlipayService {
  Map addPrePayOrder(String paramString1, String paramString2, String paramString3) throws Exception;
  
  boolean paramsSign(Map paramMap) throws Exception;
}
