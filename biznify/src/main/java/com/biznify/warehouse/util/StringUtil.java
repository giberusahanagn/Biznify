package com.biznify.warehouse.util;

public final class StringUtil {

    private StringUtil() {}

    public static String sanitize(String input) {
        return input == null ? null : input.trim().toUpperCase();
    }
}
