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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
public class EImage implements java.io.Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private EPet pet;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private EItem product;
    @ManyToOne
    @JoinColumn(name = "promotion_id", referencedColumnName = "id")
    private EPromotion promotion;
    @ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private EInvoice invoice;
    @Column(name = "image_path")
    private String imagePath;
    @CreationTimestamp
    @Column(name = "created_date")
    private String createdDate;
}
