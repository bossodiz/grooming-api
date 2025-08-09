package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_session")
@Getter
@Setter
public class EInvoiceSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="invoice_no", unique = true)
    private String invoiceNo;

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

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

}

