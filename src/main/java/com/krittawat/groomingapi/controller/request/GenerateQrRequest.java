package com.krittawat.groomingapi.controller.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GenerateQrRequest {

    private String invoiceNo;

    @NotNull
    @DecimalMin("0.01") @Digits(integer=10, fraction=2)
    private BigDecimal amount;
}
