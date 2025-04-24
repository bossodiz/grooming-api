package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "grooming_service")
public class EGroomingService implements java.io.Serializable, LocalizedName {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_th")
    private String nameTh;
    @Column(name = "name_en")
    private String nameEn;
    @ManyToOne
    @JoinColumn(name = "pet_type_id", referencedColumnName = "id")
    private EPetType petType;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "remark")
    private String remark;
    @Column(name = "sequence")
    private Integer sequence;
}
