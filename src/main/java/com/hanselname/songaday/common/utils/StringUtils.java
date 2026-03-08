package com.hanselname.songaday.common.utils;

import java.util.Optional;

public class StringUtils {

    public static String trim(String str) {
        return Optional.ofNullable(str).map(String::trim)
                       .filter(s -> !s.isEmpty()).orElse(null);
    }

    private StringUtils() {
        // No instantiation
    }
}
