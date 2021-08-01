package com.opensource.msgui.ctl.demo.controller;

import com.opensource.msgui.ctl.demo.controller.v1.autoconfiguration.EnableGuiAutoImport;
import com.opensource.msgui.ctl.demo.controller.v1.autoconfiguration.FirstClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.opensource.msgui"})
@EnableGuiAutoImport
public class CtlDemoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CtlDemoApplication.class, args);
        FirstClass firstClass = applicationContext.getBean(FirstClass.class);
        firstClass.test();
    }
}