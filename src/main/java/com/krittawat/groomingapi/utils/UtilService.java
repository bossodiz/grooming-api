package com.krittawat.groomingapi.utils;

import com.krittawat.groomingapi.datasource.entity.LocalizedName;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

import static com.krittawat.groomingapi.utils.Constants.DASH;

public class UtilService {
    public static String trimOrNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    public static String toString(Integer value) {
        if (value == null) return null;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(value);
    }

    public static String toString(BigDecimal value) {
        if (value == null) return null;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(value);
    }

    public static String toString(BigDecimal value, int decimal) {
        if (value == null) return null;
        StringBuilder pattern = new StringBuilder("#,##0");
        if (decimal > 0) {
            pattern.append(".");
            for (int i = 0; i < decimal; i++) {
                pattern.append("0");
            }
        }
        DecimalFormat df = new DecimalFormat(pattern.toString());
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(value);
    }

    public static String toStringDefaulterZero(Integer value) {
        if (value == null) return "0";
        return toString(value);
    }

    public static String toStringDefaulterZero(BigDecimal value) {
        if (value == null) value = BigDecimal.ZERO;
        return toString(value);
    }

    public static String toStringDefaulterDash(BigDecimal value) {
        if (value == null) return "-";
        return toString(value);
    }

    public static String getNameThDefaulterDash(Supplier<LocalizedName> supplier) {
        try {
            LocalizedName obj = supplier.get();
            return obj != null ? obj.getNameTh() : DASH;
        } catch (Exception e) {
            return DASH;
        }
    }

    public static String getNameEnDefaulterDash(Supplier<LocalizedName> supplier) {
        try {
            LocalizedName obj = supplier.get();
            return obj != null ? obj.getNameEn() : DASH;
        } catch (Exception e) {
            return DASH;
        }
    }

    public static String getNameThDefaulterDash(Enum<?> e) {
        if (e == null) return DASH;
        try {
            Method method = e.getClass().getMethod("getNameTh");
            Object name = method.invoke(e);
            return name != null ? name.toString() : DASH;
        } catch (Exception ex) {
            return DASH;
        }
    }

    public static String getNameEnDefaulterDash(Enum<?> e) {
        if (e == null) return DASH;
        try {
            Method method = e.getClass().getMethod("getNameEn");
            Object name = method.invoke(e);
            return name != null ? name.toString() : DASH;
        } catch (Exception ex) {
            return DASH;
        }
    }

    public static String getEnum(Enum<?> e) {
        if (e == null) return null;
        return e.name();
    }

    public static String getEnumName(Enum<?> e) {
        if (e == null) return null;
        try {
            Method method = e.getClass().getMethod("getName");
            Object name = method.invoke(e);
            return name != null ? name.toString() : null;
        } catch (Exception ex) {
            return null;
        }
    }

    public static BigDecimal toBigDecimal(String value) {
        if (StringUtils.hasText(value)) {
            return new BigDecimal(value);
        } else {
            return null;
        }
    }

    public static String toString(LocalDateTime value) {
        if (value == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return value.format(formatter);
    }
}
