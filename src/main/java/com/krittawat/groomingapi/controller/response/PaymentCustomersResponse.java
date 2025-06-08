package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaymentCustomersResponse {
    private Long key;
    private String label;
    private String name;
    private List<PetResponse> pets;
}
