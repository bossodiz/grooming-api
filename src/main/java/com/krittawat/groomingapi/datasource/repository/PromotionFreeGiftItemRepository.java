package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EPromotionFreeGiftItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionFreeGiftItemRepository extends JpaRepository<EPromotionFreeGiftItem, Long> {

    List<EPromotionFreeGiftItem> findByPromotionId(Long promotionId);

}
