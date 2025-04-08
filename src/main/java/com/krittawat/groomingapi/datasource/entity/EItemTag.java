package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "item_tag", schema = "dev")
public class EItemTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "item_info_id", nullable = false)
    private Long itemInfoId;

    @Size(max = 100)
    @NotNull
    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName;

}