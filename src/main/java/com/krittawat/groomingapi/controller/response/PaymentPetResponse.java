package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentPetResponse {
    private Long key;
    private String label;
    private String name;
    private Long petTypeId;
}
