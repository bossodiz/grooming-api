package com.krittawat.groomingapi.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetResponse {
    private Long id;
    private String name;
    private Integer ageYear;
    private Integer ageMonth;
    private String gender;
    private Long breedId;
    private Long typeId;
    private String genderTh;
    private String genderEn;
    private String breedNameTh;
    private String breedNameEn;
    private String typeNameTh;
    private String typeNameEn;
    private String weight;
    private String service;
    private LocalDateTime lastedServiceDate;
    private Long customerId;
    private String customerName;
    private String customerPhone;
}
