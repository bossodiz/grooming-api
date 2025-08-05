package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.response.PromotionResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EPromotion;
import com.krittawat.groomingapi.datasource.service.PromotionService;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionDiscountService {

    private final PromotionService promotionService;

    public Response getList() {
        List<EPromotion> list = promotionService.findAll();
        List<PromotionResponse> promotions = new ArrayList<>();
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
                .status(promotion.getStatus())
                .quota(promotion.getQuota())
                .condition(promotion.getCondition())
            .build());
        }
        return Response.builder()
                .data(promotions)
                .build();
    }

}
