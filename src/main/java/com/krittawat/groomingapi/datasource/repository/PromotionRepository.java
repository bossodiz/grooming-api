package com.krittawat.groomingapi.datasource.repository;

import com.krittawat.groomingapi.datasource.entity.EPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<EPromotion, Long> {

    @Query("SELECT p FROM EPromotion p WHERE p.status = true AND p.startDate <= CURRENT_DATE AND p.endDate >= CURRENT_DATE")
    List<EPromotion> findByStatusAndStartDateEndDate();

    @Modifying
    @Query("""
      UPDATE EPromotion p
      SET p.quota = p.quota - :amount
      WHERE p.id = :promotionId
        AND p.quota IS NOT NULL
        AND p.quota <> -1
        AND p.quota >= :amount
      """)
    int consumeQuota(@Param("promotionId") Long promotionId, @Param("amount") int amount);

}