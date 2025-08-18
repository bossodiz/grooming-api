package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPromotionIncludedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionIncludedItemRepository extends JpaRepository<EPromotionIncludedItem, Long> {

    int countByPromotionId(Long promotionId);

    boolean existsByPromotionIdAndItemId(Long promotionId, Long itemId);

    @Query("SELECT i FROM EPromotionIncludedItem pi JOIN EItem i ON pi.itemId = i.id WHERE pi.promotionId = ?1 ORDER BY i.id")
    List<EItem> findAllByPromotionId(Long promotionId);

}
