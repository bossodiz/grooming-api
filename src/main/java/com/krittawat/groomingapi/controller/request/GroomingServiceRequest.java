package com.krittawat.groomingapi.controller.request;

import lombok.Data;

@Data
public class GroomingServiceRequest {
    private Long id;
    private String name;
    private String description;
    private Long type;
    private String remark;
    private String price;
    private String barcode;
}
