package com.krittawat.groomingapi.datasource.service;

import com.krittawat.groomingapi.controller.response.FreeGiftResponse;
import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPromotion;
import com.krittawat.groomingapi.datasource.entity.EPromotionFreeGiftItem;
import com.krittawat.groomingapi.datasource.repository.ItemsRepository;
import com.krittawat.groomingapi.datasource.repository.PromotionFreeGiftItemRepository;
import com.krittawat.groomingapi.datasource.repository.PromotionRepository;
import com.krittawat.groomingapi.error.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
