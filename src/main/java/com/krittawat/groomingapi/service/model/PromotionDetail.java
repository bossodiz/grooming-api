package com.krittawat.groomingapi.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromotionDetail {
    private Long id;
    private String name;

    @JsonProperty("discount_category")
    private String discountCategory;

    @JsonProperty("discount_type")
    private EnumUtil.DISCOUNT_TYPE discountType;

    @JsonProperty("amount_normal")
    private String amountNormal;

    @JsonProperty("amount_more_than")
    private String amountMoreThan;

    @JsonProperty("discount_more_than")
    private String discountMoreThan;

    @JsonProperty("amount_free")
    private String amountFree;

    @JsonProperty("amount_free_bonus")
    private String amountFreeBonus;

    @JsonProperty("amount_type")
    private EnumUtil.AMOUNT_TYPE amountType;
    @JsonProperty("period_type")
    private EnumUtil.PERIOD_TYPE periodType;
    @JsonProperty("start_date")
    private LocalDateTime startDate;
    @JsonProperty("end_date")
    private LocalDateTime endDate;
    @JsonProperty("specific_days")
    private List<String> specificDays;
    @JsonProperty("customer_only")
    private Boolean customerOnly;
    private Boolean status;
    private Integer quota;
    @JsonProperty("quota_type")
    private Integer quotaType;
    private String condition;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
