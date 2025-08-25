package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.PromotionDetailRequest;
import com.krittawat.groomingapi.controller.response.PromotionItem;
import com.krittawat.groomingapi.controller.response.PromotionResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPromotion;
import com.krittawat.groomingapi.datasource.service.ItemsService;
import com.krittawat.groomingapi.datasource.service.PromotionService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.model.AmountDetail;
import com.krittawat.groomingapi.utils.EnumUtil;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Response updatePromotion(PromotionDetailRequest request) throws DataNotFoundException {
        EPromotion promotion = request.getPromotionId() != null ? promotionService.getById(request.getPromotionId()) : new EPromotion();
        promotion.setName(request.getPromotionDetail().getName());
        promotion.setDiscountCategory(EnumUtil.PROMOTION_TYPE.valueOf(request.getPromotionDetail().getDiscountCategory()));
        promotion.setDiscountType(request.getPromotionDetail().getDiscountType());
        promotion.setAmountType(request.getPromotionDetail().getAmountType());
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
        promotionService.save(promotion);
//        if (request.getIncludeItems() != null) {
//            itemsService.updatePromotionItems(id, request.getIncludeItems(), true);
//        }
//        if (request.getExcludeItems() != null) {
//            itemsService.updatePromotionItems(id, request.getExcludeItems(), false);
//        }

        return Response.builder()
                .code(200)
                .data("Update promotion success")
                .build();
    }
}
