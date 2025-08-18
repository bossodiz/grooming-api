package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromotionItem {
    private Long id;
    private String name;
    private String description;
    private String price;
}
