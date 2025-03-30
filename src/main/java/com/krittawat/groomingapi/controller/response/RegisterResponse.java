package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String nickname;
    private String email;
    private String phone1;
    private String phone2;
}
