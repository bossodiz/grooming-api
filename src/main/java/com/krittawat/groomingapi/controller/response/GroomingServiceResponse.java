package com.krittawat.groomingapi.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroomingServiceResponse {
    private Long id;
    private String name;
    private String price;
    private String remark;
    private Long type;
    private String typeName;
    private String description;
    private String barcode;
    private List<ItemTagResponse> tags;
}
