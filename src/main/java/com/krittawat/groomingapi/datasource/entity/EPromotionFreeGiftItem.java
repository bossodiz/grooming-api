package com.krittawat.groomingapi.datasource.entity;

import jakarta.persistence.*;
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
@Table(name = "promotion_free_gift_item")
public class EPromotionFreeGiftItem {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "promotion_id")
    private Long promotionId;
    @Column(name = "buy_item_id")
    private Long buyItemId;
    @Column(name = "free_item_id")
    private Long freeItemId;
    @CreationTimestamp
    @Column(name = "created_at")
    private String createdAt;
}
