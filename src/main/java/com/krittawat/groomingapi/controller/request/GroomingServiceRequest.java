package com.krittawat.groomingapi.controller.request;

import lombok.Data;

@Data
public class GroomingServiceRequest {
    private Long id;
    private String nameTh;
    private String nameEn;
    private String remark;
    private Long petTypeId;
    private String price;
}
