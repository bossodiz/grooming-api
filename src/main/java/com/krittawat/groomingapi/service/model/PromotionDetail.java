package com.krittawat.groomingapi.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromotionDetail {
    private Long id;
    private String name;
    @JsonProperty("discount_category")
    private String discountCategory;
    @JsonProperty("discount_type")
    private String discountType;
    private String amount;
    @JsonProperty("amount_type")
    private String amountType;
    @JsonProperty("period_type")
    private String periodType;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("specific_days")
    private String specificDays;
    @JsonProperty("customer_only")
    private Boolean customerOnly;
    private Boolean status;
    private Integer quota;
    @JsonProperty("quota_type")
    private Integer quotaType;
    private String condition;
    @JsonProperty("condition_value")
    private String conditionValue;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
}
