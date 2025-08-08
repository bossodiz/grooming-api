package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.controller.response.AppliedPromotionResponse;
import com.krittawat.groomingapi.controller.response.CartCalculationResultResponse;
import com.krittawat.groomingapi.controller.response.CartItemResponse;
import com.krittawat.groomingapi.controller.response.FreeGiftResponse;
import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPromotion;
import com.krittawat.groomingapi.datasource.entity.EPromotionFreeGiftItem;
import com.krittawat.groomingapi.datasource.repository.ItemsRepository;
import com.krittawat.groomingapi.datasource.repository.PromotionFreeGiftItemRepository;
import com.krittawat.groomingapi.datasource.repository.PromotionRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionFreeGiftItemRepository promotionFreeGiftItemRepository;
    private final ItemsRepository itemsRepository;

    public List<EPromotion> findAll() {
        return promotionRepository.findAll();
    }

    public List<EPromotion> findByActive() {
        return promotionRepository.findByStatusAndStartDateEndDate();
    }

    public List<EPromotionFreeGiftItem> getFreeGiftByPromotionId(Long promotionId) {
        return promotionFreeGiftItemRepository.findByPromotionId(promotionId);
    }


    public List<FreeGiftResponse> getAllFreeGifts() {
        // ดึงข้อมูล promotion free gift ทั้งหมดจาก DB
        List<EPromotionFreeGiftItem> freeGiftEntities = promotionFreeGiftItemRepository.findAll();

        // แปลงข้อมูลเป็น FreeGiftResponse list
        return freeGiftEntities.stream().map(entity -> {
            // ดึงชื่อสินค้าของ buyItemId และ freeItemId จาก itemsService
            String buyItemName = "";
            String freeItemName = "";
            try {
                EItem buyItem = itemsRepository.findById(entity.getBuyItemId()).orElseThrow(()-> new DataNotFoundException("Buy item not found with id: " + entity.getBuyItemId()));
                buyItemName = buyItem != null ? buyItem.getName() : "";
            } catch (DataNotFoundException e) {
                buyItemName = "";
            }
            try {
                EItem freeItem = itemsRepository.findById(entity.getFreeItemId()).orElseThrow(()-> new DataNotFoundException("Buy item not found with id: " + entity.getBuyItemId()));
                freeItemName = freeItem != null ? freeItem.getName() : "";
            } catch (DataNotFoundException e) {
                freeItemName = "";
            }

            return FreeGiftResponse.builder()
                    .buyItemId(entity.getBuyItemId())
                    .buyItemName(buyItemName)
                    .freeItemId(entity.getFreeItemId())
                    .freeItemName(freeItemName)
                    .alreadyInCart(false)   // default ตอนนี้ยังไม่ได้ตรวจสอบใส่ตะกร้า
                    .inCartQuantity(0)
                    .build();
        }).toList();
    }

    public EPromotion getById(Long promoId) throws DataNotFoundException {
        Optional<EPromotion> promotion = promotionRepository.findById(promoId);
        if (promotion.isEmpty()) {
            throw new DataNotFoundException("Promotion not found with id: " + promoId);
        }
        return promotion.get();
    }

    @Transactional
    public void consumePromotionQuotas(CartCalculationResultResponse previewResult) throws DataNotFoundException {
        Map<Long, Integer> consumeMap = new HashMap<>();
        for (CartItemResponse it : previewResult.getItems()) {
            if (it.getAppliedPromotions() == null) continue;

            for (AppliedPromotionResponse ap : it.getAppliedPromotions()) {
                if (ap.getPromotionId() == null) continue;
                int units = 0;
                // ถ้าเป็น FREE → ใช้จำนวนของแถมจริง
                EPromotion promo = getById(ap.getPromotionId());
                if (promo.getDiscountType() == EnumUtil.DISCOUNT_TYPE.FREE) {
                    units = (ap.getUsedUnits() != null) ? ap.getUsedUnits() : 0;
                }
                if (units > 0) {
                    consumeMap.merge(ap.getPromotionId(), units, Integer::sum);
                }
            }
        }
        // ยิง UPDATE อะตอมมิกต่อโปรฯ
        for (Map.Entry<Long, Integer> e : consumeMap.entrySet()) {
            Long promoId = e.getKey();
            int amount = e.getValue();

            // ข้ามถ้า quota ไม่จำกัด
            EPromotion p = getById(promoId);
            if (p.getQuota() == null || p.getQuota() == -1) continue;

            int updated = consumeQuota(promoId, amount);
            if (updated == 0) {
                throw new IllegalStateException("Quota not enough for promotion: " + p.getName());
            }
        }
    }

    public int consumeQuota(Long promoId, int amount) throws DataNotFoundException {
        int updatedRows = promotionRepository.consumeQuota(promoId, amount);
        if (updatedRows == 0) {
            throw new DataNotFoundException("Promotion quota not available or insufficient for id: " + promoId);
        }
        return updatedRows;
    }
}
