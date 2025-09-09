package com.krittawat.groomingapi.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.krittawat.groomingapi.service.model.PromotionDetail;
import com.krittawat.groomingapi.service.model.PromotionItemWrapper;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PromotionDetailRequest {
    private Long promotionId;
    private PromotionDetail promotionDetail;
    private PromotionItemWrapper included;
    private PromotionItemWrapper excluded;
    private PromotionItemWrapper bought;
    private PromotionItemWrapper free;
}
