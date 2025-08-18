package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPromotionExcludedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionExcludedItemRepository extends JpaRepository<EPromotionExcludedItem, Long> {

    int countByPromotionId(Long promotionId);

    boolean existsByPromotionIdAndItemId(Long promotionId, Long itemId);

    @Query("SELECT i FROM EPromotionExcludedItem pei JOIN EItem i ON pei.itemId = i.id WHERE pei.promotionId = ?1 ORDER BY i.id")
    List<EItem> findAllByPromotionId(Long id);
}
