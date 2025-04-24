package com.krittawat.groomingapi.controller.request;

import lombok.Data;

@Data
public class ReserveRequest {
    private Long id;
    private Long pet;
    private String nameOther;
    private String phone;
    private Long type;
    private Long breed;
    private Long grooming;
    private String end; //"2025-04-24T09:00:00"
    private String start; //"2025-04-24T09:00:00"
    private String note;
    private String color;
}
