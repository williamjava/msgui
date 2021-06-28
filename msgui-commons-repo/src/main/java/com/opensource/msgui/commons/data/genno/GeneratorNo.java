package com.opensource.msgui.commons.data.genno;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

public class GeneratorNo {
  public static String genNo(String prefix) {
    long id = IdWorker.getId();
    String s = String.valueOf(id);
    String substring = s.substring(1, s.length());
    return prefix + substring;
  }
  
  public static String genNo(String prefix, String sub) {
    return prefix + sub;
  }
}
