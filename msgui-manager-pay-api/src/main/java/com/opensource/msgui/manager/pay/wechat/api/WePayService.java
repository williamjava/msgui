package com.opensource.msgui.manager.pay.wechat.api;

import java.util.Map;

public interface WePayService {
  Map addPrePayOrder(String paramString1, String paramString2, Long paramLong, String paramString3) throws Exception;
  
  Map addH5PayOrder(String paramString1, String paramString2, Long paramLong, String paramString3, String paramString4) throws Exception;
}
