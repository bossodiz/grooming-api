package com.krittawat.groomingapi.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class CalculateRequest {
    private List<CartItemRequest> items;
    private String calculationId;
}
