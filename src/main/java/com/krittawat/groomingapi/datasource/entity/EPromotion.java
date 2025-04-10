package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotion")
public class EPromotion implements java.io.Serializable, LocalizedName {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_th")
    private String nameTh;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "promotion_type")
    private String promotionType;
    @ManyToOne
    @JoinColumn(name = "grooming_service_id", referencedColumnName = "id")
    private EGroomingService groomingService;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private EProduct product;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "expire_date")
    private LocalDateTime expireDate;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "amount_type")
    private String amountType;
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
