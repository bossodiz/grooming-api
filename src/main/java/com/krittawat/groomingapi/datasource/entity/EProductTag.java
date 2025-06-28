package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_tag")
public class EProductTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private EProduct eProduct;

    @Size(max = 100)
    @NotNull
    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName;

}