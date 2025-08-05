package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EPromotionExcludedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionExcludedItemRepository extends JpaRepository<EPromotionExcludedItem, Long> {

    int countByPromotionId(Long promotionId);

    boolean existsByPromotionIdAndItemId(Long promotionId, Long itemId);
}
