package com.krittawat.groomingapi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class EnumUtil {

    @Getter
    @AllArgsConstructor
    public enum Role {
        ADMIN("แอดมิน", "Admin"),
        CUSTOMER("ลูกค้า", "Customer");
        private final String nameTh;
        private final String nameEn;
    }

    public enum PET {
        DOG, CAT,
    }

    @Getter
    @AllArgsConstructor
    public enum GENDER {
        MALE("ผู้", "Male"),
        FEMALE("เมีย", "Female"),
        UNKNOWN("-","-");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum PAYMENT_TYPE {
        CASH_PAYMENT("เงินสด", "Cash"),
        QR_PAYMENT("QR Code", "QR Code");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum ORDER_TYPE {
        GROOMING("กรูมมิ่ง", "Grooming"),
        PRODUCT("สินค้า", "Product");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum PROMOTION_TYPE {
        GROOMING("กรูมมิ่ง", "Grooming"),
        PRODUCT("สินค้า", "Product"),
        ALL("ทั้งหมด", "All");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum AMOUNT_TYPE {
        BAHT("บาท", "Baht"),
        PERCENT("%", "Percent"),
        FREE("ฟรี", "Free");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum LOCALE {
        TH("ไทย", "Thai"),
        EN("อังกฤษ", "English");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum RESERVATION_TYPE {
        GROOMING("Grooming", "Grooming");
        private final String nameTh;
        private final String nameEn;
    }
}
