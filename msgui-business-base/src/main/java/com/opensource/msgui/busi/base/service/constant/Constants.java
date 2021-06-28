package com.opensource.msgui.busi.base.service.constant;

public interface Constants {
  public static interface InventoryLocatorState {
    public static final String CS = "0";
    
    public static final String PDZ = "1";
    
    public static final String PDJS = "2";
  }
  
  public static interface StockInState {
    public static final String STOCKINING = "1";
    
    public static final String WAIT_REVIEW = "2";
    
    public static final String AUDITED = "3";
  }
  
  public static interface ReceiveType {
    public static final String IR_NONE = "0";
    
    public static final String IR_PURCHASE = "1";
  }
  
  public static interface ReceiveState {
    public static final String WAIT_RECEIVE = "0";
    
    public static final String RECEIVEING = "1";
    
    public static final String WAIT_REVIEW = "2";
    
    public static final String AUDITED = "3";
  }
  
  public static interface PurchaseState {
    public static final String UNFINISH = "0";
    
    public static final String UNREVIEW = "1";
    
    public static final String REVIEWED = "2";
    
    public static final String PURCHASE_ING = "3";
    
    public static final String PRE_INSTOCK_ING = "4";
    
    public static final String CHKE_PRE_INSTOCK_ING = "5";
    
    public static final String PURCHASE_FINSH = "6";
  }
}
