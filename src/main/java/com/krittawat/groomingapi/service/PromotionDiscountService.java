package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.response.PromotionItem;
import com.krittawat.groomingapi.controller.response.PromotionResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPromotion;
import com.krittawat.groomingapi.datasource.service.ItemsService;
import com.krittawat.groomingapi.datasource.service.PromotionService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionDiscountService {

    private final PromotionService promotionService;
    private final ItemsService itemsService;

    public Response getList() {
        List<EPromotion> list = promotionService.findAll();
        List<PromotionResponse> promotions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (EPromotion promotion : list) {
            promotions.add(PromotionResponse.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .discountCategory(UtilService.getNameThDefaulterDash(promotion.getDiscountCategory()))
                .discountType(UtilService.getNameThDefaulterDash(promotion.getDiscountType()))
                .amount(UtilService.toString(promotion.getAmount(), 0))
                .amountType(UtilService.getNameThDefaulterDash(promotion.getAmountType()))
                .periodType(UtilService.getNameThDefaulterDash(promotion.getPeriodType()))
                .startDate(UtilService.toString(promotion.getStartDate()))
                .endDate(UtilService.toString(promotion.getEndDate()))
                .specificDays(UtilService.getNameThDefaulterDash(promotion.getSpecificDays()))
                .customerOnly(promotion.getCustomerOnly())
                .status(getStatus(promotion, now))
                .quota(promotion.getQuota())
                .condition(promotion.getCondition())
            .build());
        }
        return Response.builder()
                .data(promotions)
                .build();
    }

    private static String getStatus(EPromotion promotion, LocalDateTime now) {
        String status = "ACTIVE";
        if (Boolean.TRUE == promotion.getStatus()) {
            if (promotion.getStartDate() != null && promotion.getEndDate() != null) {
                if (promotion.getStartDate().isAfter(now)) {
                    status = "UPCOMING";
                } else if (promotion.getEndDate().isBefore(now)) {
                    status = "EXPIRED";
                }
            }
        } else {
            status = "DISABLED";
        }
        return status;
    }

    public Response getById(Long id) throws DataNotFoundException {
        EPromotion promotion = promotionService.getById(id);
        PromotionResponse response = PromotionResponse.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .discountCategory(promotion.getDiscountCategory().name())
                .discountType(promotion.getDiscountType().name())
                .amount(UtilService.toString(promotion.getAmount(), 0))
                .amountType(promotion.getAmountType().name())
                .periodType(promotion.getPeriodType().name())
                .startDate(UtilService.toStringFull(promotion.getStartDate()))
                .endDate(UtilService.toStringFull(promotion.getEndDate()))
                .specificDays(promotion.getSpecificDays() != null ? promotion.getSpecificDays().name() : null)
                .customerOnly(promotion.getCustomerOnly())
                .isStatus(promotion.getStatus())
                .quota(promotion.getQuota())
                .condition(promotion.getCondition())
                .createdAt(UtilService.toStringFull(promotion.getCreatedAt()))
                .updatedAt(UtilService.toStringFull(promotion.getUpdatedAt()))
            .build();
        return Response.builder()
                .data(response)
                .build();
    }

    public Response getPromotionItems(EnumUtil.ITEM_TYPE type) {
        List<EItem> items = itemsService.getItemsByType(type);
        List<PromotionItem> itemList = items.stream().sorted(Comparator.comparing(EItem::getName))
                .map(item -> PromotionItem.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .price(UtilService.toString(item.getPrice(), 2))
                    .build())
                .toList();
        return Response.builder()
                .code(200)
                .data(itemList)
                .build();
    }

    public Response getPromotionIncludeItems(Long id) {
        List<EItem> items = itemsService.getItemsByPromotionIdAndInclude(id);
        List<PromotionItem> itemList = items.stream()
                .map(item -> PromotionItem.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(UtilService.toString(item.getPrice(), 2))
                        .build())
                .toList();
        return Response.builder()
                .code(200)
                .data(itemList)
                .build();
    }

    public Response getPromotionExcludeItems(Long id) {
        List<EItem> items = itemsService.getItemsByPromotionIdAndExclude(id);
        List<PromotionItem> itemList = items.stream()
                .map(item -> PromotionItem.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(UtilService.toString(item.getPrice(), 2))
                        .build())
                .toList();
        return Response.builder()
                .code(200)
                .data(itemList)
                .build();
    }
}
