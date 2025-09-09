package com.krittawat.groomingapi.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppliedPromotionResponse {
    private Long promotionId;
    private String name;
    private BigDecimal discountAmount;
    private Integer usedUnits; // ถ้ามี
}