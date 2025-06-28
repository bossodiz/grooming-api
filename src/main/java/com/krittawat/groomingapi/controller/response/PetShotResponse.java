package com.krittawat.groomingapi.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetShotResponse {
    private Long id;
    private String nameTh;
    private String nameEn;
    private String description;
    private String tag;
    private String price;
    private String stock;
    private List<ItemTagResponse> tags;
}
