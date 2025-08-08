package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculation_session")
@Getter
@Setter
public class ECalculationSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="calculation_id", unique = true)
    private String calculationId;

    @Column(name="cart_hash")
    private String cartHash;

    @Column(name="payload_json", columnDefinition = "json")
    private String payloadJson;

    @Column(name="total_before")
    private BigDecimal totalBefore;

    @Column(name="total_discount")
    private BigDecimal totalDiscount;

    @Column(name="total_after")
    private BigDecimal totalAfter;

    @Column(name="status")
    private String status; // PREVIEW / FINALIZED / EXPIRED

    @Column(name="expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="finalized_at")
    private LocalDateTime finalizedAt;
}

