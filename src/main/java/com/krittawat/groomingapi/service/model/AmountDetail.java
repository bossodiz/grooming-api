package com.krittawat.groomingapi.service.model;

import com.krittawat.groomingapi.utils.EnumUtil;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
public class AmountDetail {
    private String detail;
    private String amountNormal;
    private String amountMoreThan;
    private String discountMoreThan;
    private String amountFree;
    private String amountFreeBonus;
    private String amountType;

    public AmountDetail(EnumUtil.DISCOUNT_TYPE discountType, String condition, BigDecimal amount, EnumUtil.AMOUNT_TYPE amountType) {
        DecimalFormat df = new DecimalFormat("#,##0");
        switch (discountType) {
            case NORMAL:
                amountNormal = UtilService.toString(amount, 0);
                detail = "ส่วนลด " + amountNormal + " " + UtilService.getNameThDefaulterDash(amountType);
                break;
            case MORE_THAN:
                amountMoreThan = UtilService.extractConditionAmount(condition).toPlainString();
                discountMoreThan = UtilService.toString(amount, 0);
                detail = String.format("ซื้อมากกว่า %s บาท ลด %s %s", df.format(Integer.valueOf(amountMoreThan)), df.format(Integer.valueOf(discountMoreThan)), UtilService.getNameThDefaulterDash(amountType));
                break;
            case FREE:
                amountFree = String.valueOf(UtilService.extractRequiredQuantity(condition));
                amountFreeBonus = UtilService.toString(amount, 0);
                detail = String.format("ซื้อ %s แถม %s", df.format(Integer.valueOf(amountFree)), df.format(Integer.valueOf(amountFreeBonus)));
                break;
        }
    }
}
