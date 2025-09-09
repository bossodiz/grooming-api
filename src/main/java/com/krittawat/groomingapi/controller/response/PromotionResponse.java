package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PromotionResponse {
    private Long   id;
    private String name;
    private String discountCategory;

    private String discountType;
    private String amount;
    private String amountNormal;
    private String amountMoreThan;
    private String discountMoreThan;
    private String amountFree;
    private String amountFreeBonus;

    private String amountType;
    private String periodType;
    private String periodDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> specificDays;
    private Boolean customerOnly;
    private String status;
    private Boolean isStatus;
    private Integer quota;
    private Integer quotaType;
    private String condition;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
