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
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
public class EItem {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "barcode")
    private String barcode;
    @Column(name = "remark")
    private String remark;
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type")
    private EnumUtil.ITEM_TYPE itemType;
    @Enumerated(EnumType.STRING)
    @Column(name = "item_category")
    private EnumUtil.ITEM_CATEGORY itemCategory;


    @ManyToMany
    @JoinTable(
            name = "item_tag",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<ETag> tags;
}
