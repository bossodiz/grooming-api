package com.krittawat.groomingapi.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemTag {
    private Long tagId;
    private Long itemId;
    private String tagName;
}
