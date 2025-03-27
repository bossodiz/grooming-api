package com.krittawat.groomingapi.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    private String firstname;
    private String lastname;
    private String nickname;
    private String email;
    @NotNull
    private String phone1;
    private String phone2;
}
