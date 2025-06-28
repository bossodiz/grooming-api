package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemTagResponse {
    private Long id;
    private String name;
    private List<Long> itemIds;
}
