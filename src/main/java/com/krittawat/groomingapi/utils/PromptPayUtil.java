package com.krittawat.groomingapi.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PromptPayUtil {
    private PromptPayUtil(){}

    private static String tlv(String tag, String value){
        return tag + String.format("%02d", value.length()) + value;
    }

    private static String normalizePhone(String phone){
        String digits = phone.replaceAll("\\D", "");
        if (digits.startsWith("0")) return "0066" + digits.substring(1);
        if (digits.startsWith("66")) return "00" + digits;
        if (digits.startsWith("0066")) return digits;
        return "0066" + digits;
    }

    public static String buildPayloadPhone(String phone, BigDecimal amount){
        return buildPayload("01", normalizePhone(phone), amount);
    }
    public static String buildPayloadNationalId(String id13, BigDecimal amount){
        return buildPayload("02", id13.replaceAll("\\D",""), amount);
    }

    private static String buildPayload(String subTag, String accValue, BigDecimal amount){
        String id00 = tlv("00","01");
        String id01 = tlv("01", amount != null ? "12" : "11"); // 12 = dynamic, 11 = static

        String aid  = tlv("00","A000000677010111"); // PromptPay AID
        String sub  = tlv(subTag, accValue);        // 01=phone, 02=nationalId
        String id29 = tlv("29", aid + sub);         // Merchant Account Info

        String id52 = tlv("52","0000");             // MCC optional
        String id53 = tlv("53","764");              // THB
        String id54 = (amount != null) ? tlv("54", amount.setScale(2, RoundingMode.HALF_UP).toPlainString()) : "";
        String id58 = tlv("58","TH");               // Country

        String partial = id00 + id01 + id29 + id52 + id53 + id54 + id58 + "6304";
        String crc = crc16(partial);
        return partial + crc;
    }

    // CRC16-CCITT (poly 0x1021, init 0xFFFF)
    private static String crc16(String data){
        int crc = 0xFFFF;
        for (char ch : data.toCharArray()){
            crc ^= (ch << 8);
            for (int i=0;i<8;i++){
                if ((crc & 0x8000) != 0) crc = (crc << 1) ^ 0x1021;
                else crc <<= 1;
                crc &= 0xFFFF;
            }
        }
        return String.format("%04X", crc);
    }
}