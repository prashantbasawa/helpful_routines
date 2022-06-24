package com.pgb.logging;

import lombok.experimental.UtilityClass;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

@UtilityClass
public class MdcThreadHelper {

    public static final String TRACE_ID = "TRACE_ID";

    public static <T> Supplier<T> tracedSupplier(Supplier<T> supplier) {
        String traceId = MDC.get(TRACE_ID);

        return () -> {
            try {
                MDC.put(TRACE_ID, traceId);
                return supplier.get();
            } finally {
                MDC.remove(TRACE_ID);
            }
        };
    }

    public static <T> Callable<T> tracedCallable(Callable<T> callable) {
        String traceId = MDC.get(TRACE_ID);

        return () -> {
            try {
                MDC.put(TRACE_ID, traceId);
                return callable.call();
            } finally {
                MDC.remove(TRACE_ID);
            }
        };
    }

    public static Runnable tracedRunnable(Runnable runnable) {
        String traceId = MDC.get(TRACE_ID);

        return () -> {
            try {
                MDC.put(TRACE_ID, traceId);
                runnable.run();
            } finally {
                MDC.remove(TRACE_ID);
            }
        };
    }
}
