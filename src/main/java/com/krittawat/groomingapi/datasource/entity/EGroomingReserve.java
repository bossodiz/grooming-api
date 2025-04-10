package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "grooming_reserve")
public class EGroomingReserve implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private EUser customer;
    @Column(name = "customer_other")
    private String customerOther;
    @Column(name = "phone")
    private String phone;
    @ManyToOne
    @JoinColumn(name = "pet_type_id", referencedColumnName = "id")
    private EPetType petType;
    @ManyToOne
    @JoinColumn(name = "pet_breed_id", referencedColumnName = "id")
    private EPetBreed petBreed;
    @Column(name = "reserve_date")
    private LocalDateTime reserveDate;
    @Column(name = "reserve_time")
    private Time reserveTime;
    @ManyToOne
    @JoinColumn(name = "grooming_service_id", referencedColumnName = "id")
    private EGroomingService groomingService;
    @CreationTimestamp
    @Column(name = "created_date")
    private String createdDate;
}
