package com.krittawat.groomingapi.controller.request;

import com.krittawat.groomingapi.service.model.ManualDiscount;
import lombok.Data;

import java.util.List;

@Data
public class CalculateRequest {
    private List<CartItemRequest> items;
    private String invoiceNo;
    private ManualDiscount manualDiscount;
}
