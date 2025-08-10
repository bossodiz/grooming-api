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
        CASH_PAYMENT("CASH"),
        QR_PAYMENT("QR");
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public enum PROMOTION_TYPE {
        GROOMING("กรูมมิ่ง", "Grooming"),
        PET_SHOP("สินค้า", "Pet Shop"),
        ALL("ทั้งหมด", "All");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum AMOUNT_TYPE {
        BAHT("บาท", "Baht"),
        PERCENT("%", "Percent"),
        EACH("ชิ้น", "Each");
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

    @Getter
    @AllArgsConstructor
    public enum ITEM_TYPE {
        GROOMING("GROOMING"),
        PET_SHOP("PET_SHOP");
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public enum PERIOD_TYPE {
        DATE_RANGE("ช่วงวันที่", "Date Range"),
        SPECIFIC_DAYS("วันเฉพาะ", "Specific Days");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum SPECIFIC_DAY {
        MONDAY("จันทร์", "Monday"),
        TUESDAY("อังคาร", "Tuesday"),
        WEDNESDAY("พุธ", "Wednesday"),
        THURSDAY("พฤหัสบดี", "Thursday"),
        FRIDAY("ศุกร์", "Friday"),
        SATURDAY("เสาร์", "Saturday"),
        SUNDAY("อาทิตย์", "Sunday");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum DISCOUNT_TYPE {
        NORMAL("ปกติ", "Normal"),
        NEW_CUSTOMER("ลูกค้าใหม่", "New Customer"),
        MORE_THAN("มากกว่า", "More Than"),
        FREE("ฟรี", "Free");
        private final String nameTh;
        private final String nameEn;
    }

    @Getter
    @AllArgsConstructor
    public enum ITEM_CATEGORY {
        CAT("แมว"),
        DOG("สุนัข"),
        TOY("ของเล่น"),
        FOOD("อาหาร");
        private final String name;
    }


}
