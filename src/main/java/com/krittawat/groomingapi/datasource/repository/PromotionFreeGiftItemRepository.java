package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPromotionFreeGiftItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionFreeGiftItemRepository extends JpaRepository<EPromotionFreeGiftItem, Long> {

    List<EPromotionFreeGiftItem> findByPromotionId(Long promotionId);

    @Modifying
    @Query("DELETE FROM EPromotionFreeGiftItem p WHERE p.promotionId = ?1")
    void deleteByPromotionId(Long id);


    @Query("SELECT i FROM EPromotionFreeGiftItem f INNER JOIN EItem i ON i.id = f.freeItemId WHERE f.promotionId = ?1 ORDER BY i.name ASC")
    List<EItem> findAllByPromotionIdAndIsFree(Long id);

    @Query("SELECT i FROM EPromotionFreeGiftItem f INNER JOIN EItem i ON i.id = f.buyItemId WHERE f.promotionId = ?1 ORDER BY i.name ASC")
    List<EItem> findAllByPromotionIdAndIsBought(Long id);
}
