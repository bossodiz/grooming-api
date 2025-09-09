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
@Table(name = "invoice")
public class EInvoice implements java.io.Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "invoice_no", nullable = false)
    private String invoiceNo;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private EUserProfile customer;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "payment_status")
    private Integer paymentStatus;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    @Column(name = "remark")
    private String remark;
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
