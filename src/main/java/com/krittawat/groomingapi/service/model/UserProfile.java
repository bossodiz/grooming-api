package com.krittawat.groomingapi.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile {
    private String username;
    private String firstname;
    private String lastname;
    private String nickname;
    private String email;
    private String phone1;
    private String phone2;
    private RoleProfile role;
}
