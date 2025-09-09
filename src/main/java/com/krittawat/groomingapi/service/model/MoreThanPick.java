package com.krittawat.groomingapi.service.model;

import com.krittawat.groomingapi.controller.response.AppliedPromotionResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MoreThanPick {
    private AppliedPromotionResponse overall;
    private BigDecimal moreThanDiscount;
}
