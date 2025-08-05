package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromotionResponse {
    private Long   id;
    private String name;
    private String discountCategory;
    private String discountType;
    private String amount;
    private String amountType;
    private String periodType;
    private String startDate;
    private String endDate;
    private String specificDays;
    private Boolean customerOnly;
    private Boolean status;
    private Integer quota;
    private String condition;
}
