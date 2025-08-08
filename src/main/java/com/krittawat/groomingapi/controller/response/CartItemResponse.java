package com.krittawat.groomingapi.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class CartItemResponse {
    private String key;
    private String name;
    private BigDecimal price;
    private BigDecimal total;
    private Long itemId;
    private BigDecimal discount;
    private Integer quantity;
    private BigDecimal finalTotal;
    private List<AppliedPromotionResponse> appliedPromotions;
}