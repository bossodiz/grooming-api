package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private String firstname;
    private String lastname;
    private String phone;
    private String phoneOther;
    private String email;
    private Integer serviceCount;
    private LocalDateTime createdDate;
    private LocalDateTime lastedDate;
    private List<PetResponse> pets;
}
