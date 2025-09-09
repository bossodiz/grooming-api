package com.krittawat.groomingapi.service.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ManualDiscount {
    private String type;
    private BigDecimal value;
    private BigDecimal amount;
    private String note;
}
