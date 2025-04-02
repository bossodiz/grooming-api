package com.krittawat.groomingapi.datasource.entity;

import com.krittawat.groomingapi.utils.EnumUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "pet")
public class EPet implements java.io.Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "age")
    private String age;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private EnumUtil.GENDER gender;
    @ManyToOne
    @JoinColumn(name = "pet_breed_id", referencedColumnName = "id")
    private EPetBreed petBreed;
    @Column(name = "weight")
    private BigDecimal weight;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private EUser user;
    @CreationTimestamp
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "service_count")
    private Integer serviceCount;
}
