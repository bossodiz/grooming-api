package com.krittawat.groomingapi.service.model;

import com.krittawat.groomingapi.controller.response.FreeGiftResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class DiscountResult {
    private BigDecimal amount;
    private List<FreeGiftResponse> gifts;

    public static DiscountResult noDiscount() {
        return new DiscountResult(BigDecimal.ZERO, Collections.emptyList());
    }

    public static DiscountResult of(BigDecimal amount) {
        return new DiscountResult(amount, Collections.emptyList());
    }

    public boolean hasGift() {
        return gifts != null && !gifts.isEmpty();
    }
}
