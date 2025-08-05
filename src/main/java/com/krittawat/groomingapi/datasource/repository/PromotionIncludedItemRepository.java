package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EPromotionIncludedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionIncludedItemRepository extends JpaRepository<EPromotionIncludedItem, Long> {

    int countByPromotionId(Long promotionId);

    boolean existsByPromotionIdAndItemId(Long promotionId, Long itemId);
}
