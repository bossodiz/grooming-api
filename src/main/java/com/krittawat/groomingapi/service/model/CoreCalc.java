package com.krittawat.groomingapi.service.model;

import com.krittawat.groomingapi.controller.response.CartItemResponse;
import com.krittawat.groomingapi.datasource.entity.EPromotion;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CoreCalc {
    private List<CartItemResponse> itemResponses;
    private BigDecimal totalBeforeDiscount;
    private BigDecimal totalItemDiscount;
    private BigDecimal totalAfterItemDiscount;
    private List<String> warningPromotions;
    private List<EPromotion> moreThanCandidates;
}
