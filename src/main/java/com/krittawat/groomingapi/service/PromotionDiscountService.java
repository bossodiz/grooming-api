package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.PromotionDetailRequest;
import com.krittawat.groomingapi.controller.response.PromotionItem;
import com.krittawat.groomingapi.controller.response.PromotionResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.*;
import com.krittawat.groomingapi.datasource.repository.PromotionExcludedItemRepository;
import com.krittawat.groomingapi.datasource.service.ItemsService;
import com.krittawat.groomingapi.datasource.service.PromotionService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.model.AmountDetail;
import com.krittawat.groomingapi.utils.EnumUtil;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionDiscountService {

    private final PromotionService promotionService;
    private final ItemsService itemsService;
    private final PromotionExcludedItemRepository excludedItemRepository;

    public Response getList() {
        List<EPromotion> list = promotionService.findAll();
        List<PromotionResponse> promotions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (EPromotion promotion : list) {
            AmountDetail amountDetail = new AmountDetail(promotion.getDiscountType(), promotion.getConditionValue(), promotion.getAmount(), promotion.getAmountType());
            String specificDays = null;
            if (promotion.getSpecificDays() != null) {
                List<String> day = Arrays.stream(promotion.getSpecificDays().split(",")).toList();
                if (day.size() == 5
                        && day.contains("MONDAY") && day.contains("TUESDAY")
                        && day.contains("WEDNESDAY") &&day.contains("THURSDAY")
                        && day.contains("FRIDAY")){
                    specificDays = "ทุกวันจันทร์ - ศุกร์";
                } else if (day.size() == 2 && day.contains("SATURDAY") && day.contains("SUNDAY")){
                    specificDays = "เฉพาะวันเสาร์ - อาทิตย์";
                } else if (day.size() == 7) {
                    specificDays = "ทุกวัน";
                } else if (day.size() == 1) {
                    specificDays = "เฉพาะวัน" + EnumUtil.SPECIFIC_DAY.valueOf(day.get(0)).getNameTh();
                } else if (day.size() == 6) {
                    if (!day.contains("MONDAY")) {
                        specificDays = "ทุกวัน ยกเว้นวันจันทร์";
                    } else if (!day.contains("TUESDAY")) {
                        specificDays = "ทุกวัน ยกเว้นวันอังคาร";
                    } else if (!day.contains("WEDNESDAY")) {
                        specificDays = "ทุกวัน ยกเว้นวันพุธ";
                    } else if (!day.contains("THURSDAY")) {
                        specificDays = "ทุกวัน ยกเว้นวันพฤหัสบดี";
                    } else if (!day.contains("FRIDAY")) {
                        specificDays = "ทุกวัน ยกเว้นวันศุกร์";
                    } else if (!day.contains("SATURDAY")) {
                        specificDays = "ทุกวัน ยกเว้นวันเสาร์";
                    } else if (!day.contains("SUNDAY")) {
                        specificDays = "ทุกวัน ยกเว้นวันอาทิตย์";
                    }
                } else {
                    specificDays = day.stream().map(d -> EnumUtil.SPECIFIC_DAY.valueOf(d).getNameTh()).collect(Collectors.joining(", "));
                }
            }
            String periodDate = null;
            if (promotion.getPeriodType() == EnumUtil.PERIOD_TYPE.DATE_RANGE) {
                periodDate = UtilService.toStringFullThRange(promotion.getStartDate(), promotion.getEndDate());
            } else if (promotion.getPeriodType() == EnumUtil.PERIOD_TYPE.SPECIFIC_DAYS) {
                periodDate = specificDays;
            } else {
                periodDate = "-";
            }
            promotions.add(PromotionResponse.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .discountCategory(UtilService.getNameThDefaulterDash(promotion.getDiscountCategory()))
                .amount(amountDetail.getDetail())
                .periodDate(periodDate)
                .status(getStatus(promotion, now))
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
                    status = "ENDED";
                }
            }
        } else {
            status = "DISABLED";
        }
        return status;
    }

    public Response getById(Long id) throws DataNotFoundException {
        EPromotion promotion = promotionService.getById(id);
        AmountDetail amountDetail = new AmountDetail(promotion.getDiscountType(), promotion.getConditionValue(), promotion.getAmount(), promotion.getAmountType());
        PromotionResponse response = PromotionResponse.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .discountCategory(promotion.getDiscountCategory().name())
                .discountType(promotion.getDiscountType().name())
                .amountNormal(amountDetail.getAmountNormal())
                .amountMoreThan(amountDetail.getAmountMoreThan())
                .discountMoreThan(amountDetail.getDiscountMoreThan())
                .amountFree(amountDetail.getAmountFree())
                .amountFreeBonus(amountDetail.getAmountFreeBonus())
                .amountType(promotion.getAmountType().name())
                .periodType(promotion.getPeriodType().name())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .specificDays(promotion.getSpecificDays() == null ? new ArrayList<>() : Arrays.stream(promotion.getSpecificDays().split(",")).toList())
                .customerOnly(promotion.getCustomerOnly())
                .isStatus(promotion.getStatus())
                .quota(promotion.getQuota())
                .quotaType(promotion.getQuota() == null ? 0 : 1)
                .condition(promotion.getConditionValue())
                .createdAt(promotion.getCreatedAt())
                .updatedAt(promotion.getUpdatedAt())
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

    @Transactional
    public Response updatePromotion(PromotionDetailRequest request) throws DataNotFoundException {
        EPromotion promotion = request.getPromotionId() != null ? promotionService.getById(request.getPromotionId()) : new EPromotion();
        promotion.setName(request.getPromotionDetail().getName());
        promotion.setDiscountCategory(EnumUtil.PROMOTION_TYPE.valueOf(request.getPromotionDetail().getDiscountCategory()));
        promotion.setDiscountType(request.getPromotionDetail().getDiscountType());
        promotion.setAmountType(request.getPromotionDetail().getAmountType());
        promotion.setStatus(true);
        if (promotion.getDiscountType() == EnumUtil.DISCOUNT_TYPE.NORMAL) {
            promotion.setAmount(new BigDecimal(request.getPromotionDetail().getAmountNormal()));
            promotion.setConditionValue(null);
        } else if (promotion.getDiscountType() == EnumUtil.DISCOUNT_TYPE.FREE) {
            promotion.setConditionValue("BUY_" + request.getPromotionDetail().getAmountFree());
            promotion.setAmount(new BigDecimal(request.getPromotionDetail().getAmountFreeBonus()));
        } else if (promotion.getDiscountType() == EnumUtil.DISCOUNT_TYPE.MORE_THAN) {
            promotion.setConditionValue("AMOUNT>" + request.getPromotionDetail().getAmountMoreThan());
            promotion.setAmount(new BigDecimal(request.getPromotionDetail().getDiscountMoreThan()));
        }
        promotion.setCustomerOnly(request.getPromotionDetail().getCustomerOnly());
        if (request.getPromotionDetail().getQuotaType() == 0){
            promotion.setQuota(null);
        } else {
            promotion.setQuota(request.getPromotionDetail().getQuota());
        }
        if (request.getPromotionDetail().getPeriodType() == EnumUtil.PERIOD_TYPE.DATE_RANGE) {
            promotion.setPeriodType(EnumUtil.PERIOD_TYPE.DATE_RANGE);
            promotion.setStartDate(request.getPromotionDetail().getStartDate());
            promotion.setEndDate(request.getPromotionDetail().getEndDate());
            promotion.setSpecificDays(null);
        } else if (request.getPromotionDetail().getPeriodType() == EnumUtil.PERIOD_TYPE.SPECIFIC_DAYS) {
            promotion.setPeriodType(EnumUtil.PERIOD_TYPE.SPECIFIC_DAYS);
            promotion.setStartDate(null);
            promotion.setEndDate(null);
            promotion.setSpecificDays(String.join(",", request.getPromotionDetail().getSpecificDays()));
        } else {
            promotion.setPeriodType(null);
            promotion.setStartDate(null);
            promotion.setEndDate(null);
            promotion.setSpecificDays(null);
        }
        promotion.setStatus(request.getPromotionDetail().getStatus());
        promotion.setUpdatedAt(LocalDateTime.now());
        promotionService.save(promotion);
        if (request.getPromotionDetail().getDiscountType() == EnumUtil.DISCOUNT_TYPE.FREE) {
            promotionService.deleteIncluded(promotion.getId());
            promotionService.deleteExcluded(promotion.getId());
            if (request.getBought() == null || request.getBought().getItems().isEmpty()) {
                throw new IllegalStateException("กรุณาเลือกสินค้าที่ต้องซื้ออย่างน้อย 1 รายการ");
            }
            if (request.getFree() == null || request.getFree().getItems().isEmpty()) {
                throw new IllegalStateException("กรุณาเลือกสินค้าที่แถมอย่างน้อย 1 รายการ");
            }
            promotionService.deleteFreeItems(promotion.getId());
            List<EPromotionFreeGiftItem> freeItems = new ArrayList<>();
            for (Long boughtId : request.getBought().getItems()) {
                for (Long freeId : request.getFree().getItems()) {
                    EPromotionFreeGiftItem freeItem = new EPromotionFreeGiftItem();
                    freeItem.setPromotionId(promotion.getId());
                    freeItem.setBuyItemId(boughtId);
                    freeItem.setFreeItemId(freeId);
                    freeItems.add(freeItem);
                }
            }
            promotionService.insertFreeItems(freeItems);
        } else {
            if (request.getIncluded() != null) {
                List<EPromotionIncludedItem> includeItems = promotionService.getIncludedItems(request.getPromotionId());
                if (request.getIncluded().isActive()) {
                    promotionService.deleteFreeItems(promotion.getId());
                    List<Long> itemIds = request.getIncluded().getItems().stream().toList();
                    // ลบรายการที่ถูกลบออก
                    for (EPromotionIncludedItem includeItem : includeItems) {
                        if (!itemIds.contains(includeItem.getItemId())) {
                            promotionService.deleteIncludedItem(includeItem);
                        }
                    }
                    // เพิ่มรายการใหม่ที่เพิ่มเข้ามา
                    for (Long itemId : itemIds) {
                        boolean exists = promotionService.existsIncludedItem(promotion.getId(), itemId);
                        if (!exists) {
                            EPromotionIncludedItem newInclude = new EPromotionIncludedItem();
                            newInclude.setPromotionId(promotion.getId());
                            newInclude.setItemId(itemId);
                            promotionService.insertIncludedItem(newInclude);
                        }
                    }
                } else {
                    promotionService.deleteIncluded(includeItems);
                }
            }
            if (request.getExcluded() != null) {
                List<EPromotionExcludedItem> excludedItems = promotionService.getExcludedItems(request.getPromotionId());
                if (request.getExcluded().isActive()) {
                    promotionService.deleteFreeItems(promotion.getId());
                    List<Long> itemIds = request.getExcluded().getItems().stream().toList();
                    // ลบรายการที่ถูกลบออก
                    for (EPromotionExcludedItem excludedItem : excludedItems) {
                        if (!itemIds.contains(excludedItem.getId())) {
                            promotionService.deleteExcludedItem(excludedItem);
                        }
                    }
                    // เพิ่มรายการใหม่ที่เพิ่มเข้ามา
                    for (Long itemId : itemIds) {
                        boolean exists = promotionService.existsExcludedItem(promotion.getId(), itemId);
                        if (!exists) {
                            EPromotionExcludedItem newExclude = new EPromotionExcludedItem();
                            newExclude.setPromotionId(promotion.getId());
                            newExclude.setItemId(itemId);
                            promotionService.insertExcludedItem(newExclude);
                        }
                    }
                } else {
                    promotionService.deleteExcluded(excludedItems);
                }
            }
        }
        return Response.builder()
                .code(200)
                .data(promotion.getId())
                .build();
    }


    public Response getPromotionBoughtItems(Long id) {
        List<EItem> items = itemsService.getBoughtByPromotionId(id);
        List<PromotionItem> itemList = items.stream()
                .map(item -> PromotionItem.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(UtilService.toString(item.getPrice(), 2))
                        .build())
                .distinct()
                .toList();
        return Response.builder()
                .code(200)
                .data(itemList)
                .build();
    }


    public Response getPromotionFreeItems(Long id) {
        List<EItem> items = itemsService.getFreeByPromotionId(id);
        List<PromotionItem> itemList = items.stream()
                .map(item -> PromotionItem.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(UtilService.toString(item.getPrice(), 2))
                        .build())
                .distinct()
                .toList();
        return Response.builder()
                .code(200)
                .data(itemList)
                .build();
    }


}
