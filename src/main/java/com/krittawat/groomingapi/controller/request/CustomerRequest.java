package com.krittawat.groomingapi.controller.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String name;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String phone2;
}
