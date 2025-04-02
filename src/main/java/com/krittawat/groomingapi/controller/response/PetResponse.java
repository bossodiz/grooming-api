package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetResponse {
    private Long id;
    private String name;
    private String age;
    private String gender;
    private String breed;
    private String type;
    private String weight;
    private String service;
}
