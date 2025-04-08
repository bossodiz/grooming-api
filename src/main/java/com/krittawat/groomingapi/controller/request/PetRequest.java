package com.krittawat.groomingapi.controller.request;

import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.Data;

@Data
public class PetRequest {

    private Long id;
    private String name;
    private Integer ageYear;
    private Integer ageMonth;
    private EnumUtil.GENDER gender;
    private Long type;
    private Long breed;
    private Long customerId;
    private String weight;
    private String image;
}
