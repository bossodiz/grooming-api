package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReserveGroomingResponse {

    private String className;
    private String title;
    private String start;
    private String end;
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
