package com.opensource.msgui.ctl.demo.controller.v1.autoconfiguration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author whj
 *
 * ImportSelector实现类
 */
public class GuiImportSelector implements ImportSelector {
    /**
     * 该方法返回的数组中的Bean都会加载到IOC容器
     * @param annotationMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{FirstClass.class.getName(), SecondClass.class.getName()};
    }
}
