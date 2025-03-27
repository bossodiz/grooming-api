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
    private EUser customer;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "payment_status")
    private Integer paymentStatus;
    @Column(name = "payment_date")
    private Date paymentDate;
    @Column(name = "remark")
    private String remark;
    @CreationTimestamp
    @Column(name = "created_date")
    private Date createdDate;
}
