package com.krittawat.groomingapi.datasource.entity;

import com.krittawat.groomingapi.utils.EnumUtil;
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
@Table(name = "pet")
public class EPet implements java.io.Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "age_year")
    private Integer ageYear;
    @Column(name = "age_month")
    private Integer ageMonth;
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
    private LocalDateTime createdDate;
    @Column(name = "service_count")
    private Integer serviceCount;
    @Column(name = "last_update_year")
    private LocalDateTime lastUpdateYear;
    @ManyToOne
    @JoinColumn(name = "pet_type_id", referencedColumnName = "id")
    private EPetType petType;

    public Long getBreedId() {
        return petBreed == null ? null : petBreed.getId();
    }

    public Long getTypeId() {
        return petType == null ? null : petType.getId();
    }
}
