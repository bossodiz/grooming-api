package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "item_info", schema = "dev")
public class EItemInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 500)
    @Column(name = "name", length = 500)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 100)
    @Column(name = "category", length = 100)
    private String category;

    @Size(max = 100)
    @Column(name = "price", length = 100)
    private String price;

    @Size(max = 100)
    @Column(name = "stock", length = 100)
    private String stock;

    @Size(max = 100)
    @Column(name = "barcode", length = 100)
    private String barcode;

}