package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AppliedPromotionResponse {
    private Long promotionId;
    private String name;
    private BigDecimal discountAmount;
}
