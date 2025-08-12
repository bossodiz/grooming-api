package com.krittawat.groomingapi.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.krittawat.groomingapi.service.model.ManualDiscount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartCalculationResultResponse {
    private List<CartItemResponse> items;
    private BigDecimal totalBeforeDiscount;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private List<String> warningPromotions;
    private AppliedPromotionResponse overallPromotion;
    private ManualDiscount manualDiscount;
    private String invoiceNo;
}