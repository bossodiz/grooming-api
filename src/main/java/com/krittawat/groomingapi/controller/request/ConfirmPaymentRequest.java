package com.krittawat.groomingapi.controller.request;

import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.Data;

@Data
public class ConfirmPaymentRequest {
    private String invoiceNo;
    private EnumUtil.PAYMENT_TYPE paymentType;
    private String customerId;
}
