package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReserveGroomingResponse {

    private String className;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private ExtendedProps extendedProps;

    @Data
    @Builder
    public static class ExtendedProps{
        private Long id;
        private Long pet;
        private String petName;
        private String phone;
        private Long petType;
        private Long petBreed;
        private String note;
    }
}
