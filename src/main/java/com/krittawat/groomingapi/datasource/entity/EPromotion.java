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
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotion")
public class EPromotion implements java.io.Serializable {
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
    private Date startDate;
    @Column(name = "expire_date")
    private Date expireDate;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "amount_type")
    private String amountType;
    @CreationTimestamp
    @Column(name = "created_date")
    private Date createdDate;
}
