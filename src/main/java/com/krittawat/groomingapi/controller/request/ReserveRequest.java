package com.krittawat.groomingapi.controller.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReserveRequest {
    private Long id;
    private Long pet;
    private String nameOther;
    private String phone;
    private Long type;
    private Long breed;
    private Long grooming;
    private LocalDateTime end;
    private LocalDateTime start;
    private String note;
    private String color;
}
