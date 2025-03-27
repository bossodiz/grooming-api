package com.krittawat.groomingapi.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleProfile {
    private Long id;
    private String name;
    private String description;
}
