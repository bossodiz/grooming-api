package com.krittawat.groomingapi.utils;

import org.springframework.util.StringUtils;

public class UtilService {
    public static String trimOrNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

}
