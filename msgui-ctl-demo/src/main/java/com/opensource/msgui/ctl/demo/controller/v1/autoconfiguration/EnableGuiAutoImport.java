package com.opensource.msgui.ctl.demo.controller.v1.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author whj
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({GuiImportSelector.class})
public @interface EnableGuiAutoImport {

}
