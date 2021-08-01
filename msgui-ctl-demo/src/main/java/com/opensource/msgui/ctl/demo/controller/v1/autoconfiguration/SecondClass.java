package com.opensource.msgui.ctl.demo.controller.v1.autoconfiguration;

/**
 * @author whj
 *
 * 需要装配到IOC容器中的类
 */
public class SecondClass {
    static {
        System.out.println("This is SecondClass...");
    }
}
