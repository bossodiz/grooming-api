package com.krittawat.groomingapi.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleProfile {
    private Long id;
    private String name;
    private String description;
}
