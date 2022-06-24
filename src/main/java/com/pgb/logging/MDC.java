package com.pgb.logging;

/**
 * This can be MDC implementation of any logging frameworks such as Log4j, Logback etc.
 */
public class MDC {
    public static void put(String key, String value) {}
    public static String get(String key) { return "anything"; }
    public static void remove(String key) {}
}
