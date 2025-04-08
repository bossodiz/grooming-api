package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetResponse {
    private Long id;
    private String name;
    private Integer ageYear;
    private Integer ageMonth;
    private String gender;
    private Long breedId;
    private Long typeId;
    private String breedNameTh;
    private String breedNameEn;
    private String typeNameTh;
    private String typeNameEn;
    private String weight;
    private String service;
}
