package com.krittawat.groomingapi.utils;

public class EnumUtil {

    public enum Role {
        ADMIN, CUSTOMER,
    }

    public enum PET {
        DOG, CAT,
    }

    public enum GENDER {
        MALE, FEMALE
    }

    public enum PAYMENT_TYPE {
        CASH, QR
    }

    public enum ORDER_TYPE {
        GROOMING, PRODUCT
    }

    public enum PROMOTION_TYPE {
        GROOMING, PRODUCT, ALL
    }
    public enum AMOUNT_TYPE {
        BAHT, PERCENT, FREE
    }

    public enum LOCALE {
        TH, EN
    }

}
