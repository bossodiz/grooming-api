package com.krittawat.groomingapi.datasource.entity;

import com.krittawat.groomingapi.utils.EnumUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

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
    private EUserProfile user;
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

    public record Age(int years, int months) {}

    public Age computeCurrentAge(Clock clock) {
        int baseYears  = ageYear  == null ? 0 : ageYear;
        int baseMonths = ageMonth == null ? 0 : ageMonth;

        // ถ้าไม่มี createdDate ให้ถือว่าเริ่มนับจากวันนี้ (ไม่เพิ่มเดือน)
        LocalDate start = (createdDate == null)
                ? LocalDate.now(clock)
                : createdDate.toLocalDate();

        LocalDate today = LocalDate.now(clock);
        if (today.isBefore(start)) {
            // กันข้อมูลเพี้ยน ถ้าวันที่สร้างอยู่ “อนาคต” ให้ไม่เพิ่มเดือน
            return new Age(baseYears, baseMonths % 12);
        }

        Period elapsed = Period.between(start, today);
        int elapsedMonths = elapsed.getYears() * 12 + elapsed.getMonths();

        int totalMonths = baseYears * 12 + baseMonths + elapsedMonths;
        if (totalMonths < 0) totalMonths = 0;

        int years  = totalMonths / 12;
        int months = totalMonths % 12;
        return new Age(years, months);
    }

    /** ช่วยให้เรียกแบบ default zone ได้สะดวก */
    public Age computeCurrentAge() {
        return computeCurrentAge(Clock.systemDefaultZone());
    }
}
