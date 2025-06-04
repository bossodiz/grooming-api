package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroomingServiceResponse {
    private Long id;
    private String nameTh;
    private String nameEn;
    private Long typeId;
    private String typeTh;
    private String typeEn;
    private String price;
    private String remark;
}
