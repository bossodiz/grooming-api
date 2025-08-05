package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.service.PromotionDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promotion")
@RequiredArgsConstructor
public class PromotionDiscountController {

    private final PromotionDiscountService promotionDiscountService;

    @GetMapping("/list")
    public Response getPromotion() {
        return promotionDiscountService.getList();
    }

}
