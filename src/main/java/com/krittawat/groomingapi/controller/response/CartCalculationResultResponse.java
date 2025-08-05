package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartCalculationResultResponse {
    private List<CartItemResponse> items;
    private BigDecimal totalBeforeDiscount;
    private BigDecimal totalDiscount;
    private BigDecimal totalAfterDiscount;
    private List<String> warningPromotions;
    private AppliedPromotionResponse overallPromotion;
}
