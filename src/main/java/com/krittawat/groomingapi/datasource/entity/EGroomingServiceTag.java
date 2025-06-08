package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "grooming_service_tag")
public class EGroomingServiceTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grooming_service_id", referencedColumnName = "id")
    private EGroomingService eGroomingService;

    @Size(max = 100)
    @NotNull
    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName;
}
