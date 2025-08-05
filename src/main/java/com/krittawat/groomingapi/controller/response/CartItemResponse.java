package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartItemResponse {
    private String key;
    private Long itemId;
    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal total;
    private BigDecimal discount;
    private BigDecimal finalTotal;
    private List<AppliedPromotionResponse> appliedPromotions;
}
