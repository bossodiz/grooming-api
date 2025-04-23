package com.krittawat.groomingapi.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DropdownResponse {
    private Long key;
    private Long ref_key;
    private Long ref_key2;
    private String ref_key3;
    private String value_th;
    private String value_en;
    private String image_url;
}
