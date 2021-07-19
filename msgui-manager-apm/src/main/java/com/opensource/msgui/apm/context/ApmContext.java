package com.opensource.msgui.apm.context;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;

public class ApmContext {
    public ApmContext() {
    }

    public static String traceId() {
        return TraceContext.traceId();
    }
}