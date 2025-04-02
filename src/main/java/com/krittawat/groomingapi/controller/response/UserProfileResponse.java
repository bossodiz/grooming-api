package com.krittawat.groomingapi.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponse {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String nickname;
    private String email;
    private String phone1;
    private String phone2;
    private RoleProfileResponse role;
}
