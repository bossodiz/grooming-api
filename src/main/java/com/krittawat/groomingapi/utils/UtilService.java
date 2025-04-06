package com.krittawat.groomingapi.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class UtilService {
    public static String trimOrNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
    public static String toString(BigDecimal value) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(value);
    }

    public static String toString(Integer value) {
        if (value == null) return "0";
        return String.valueOf(value);
    }
}
