package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.request.PromotionDetailRequest;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.PromotionDiscountService;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotion")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionDiscountService promotionDiscountService;

    @GetMapping("/list")
    public Response getPromotion() {
        return promotionDiscountService.getList();
    }

    @GetMapping("/{id}")
    public Response getPromotionDetail(@PathVariable("id") Long id) throws DataNotFoundException {
        return promotionDiscountService.getById(id);
    }

    @GetMapping("/items/{type}")
    public Response getPromotionItems(@PathVariable("type") EnumUtil.ITEM_TYPE type) throws DataNotFoundException {
        return promotionDiscountService.getPromotionItems(type);
    }

    @GetMapping("{id}/include")
    public Response getPromotionIncludeItems(@PathVariable("id") Long id) throws DataNotFoundException {
        return promotionDiscountService.getPromotionIncludeItems(id);
    }

    @GetMapping("{id}/exclude")
    public Response getPromotionExcludeItems(@PathVariable("id") Long id) throws DataNotFoundException {
        return promotionDiscountService.getPromotionExcludeItems(id);
    }

    @PutMapping("/update")
    public Response updatePromotion(@RequestBody PromotionDetailRequest request) throws DataNotFoundException {
        return promotionDiscountService.updatePromotion(request);
    }


}
