package com.krittawat.groomingapi.datasource.entity;

import com.krittawat.groomingapi.utils.EnumUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotion")
public class EPromotion {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_category")
    private EnumUtil.PROMOTION_TYPE discountCategory;
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private EnumUtil.DISCOUNT_TYPE discountType;
    @Enumerated(EnumType.STRING)
    @Column(name = "amount_type")
    private EnumUtil.AMOUNT_TYPE amountType;
    @Column(name = "amount")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "period_type")
    private EnumUtil.PERIOD_TYPE periodType;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "specific_days")
    private String specificDays;
    @Column(name = "customer_only")
    private Boolean customerOnly;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "quota")
    private Integer quota;
    @Column(name = "condition_value")
    private String conditionValue;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
