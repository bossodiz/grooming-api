package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order")
public class EOrder {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private EInvoice invoice;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private EUser customer;
    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private EPet pet;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private EItem item;
    @ManyToOne
    @JoinColumn(name = "promotion_id", referencedColumnName = "id")
    private EPromotion promotion;
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
