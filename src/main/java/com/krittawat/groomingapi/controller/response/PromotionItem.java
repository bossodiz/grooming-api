package com.krittawat.groomingapi.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromotionItem {
    private Long id;
    private String name;
    private String description;
    private String price;
}
