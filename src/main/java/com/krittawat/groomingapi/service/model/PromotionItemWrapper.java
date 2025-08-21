package com.krittawat.groomingapi.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromotionItemWrapper {
    private boolean active;
    private List<Long> items;
}
