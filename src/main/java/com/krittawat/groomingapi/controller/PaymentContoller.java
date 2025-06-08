package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentContoller {

    private final PaymentService paymentService;

    @GetMapping("/customers")
    public Response getCustomers() throws DataNotFoundException {
        return paymentService.getCustomers();
    }

    @GetMapping("/pets/{customerId}")
    public Response getPetsByCustomerId(@PathVariable(name="customerId") Long customerId) throws DataNotFoundException {
        return paymentService.getPetsByCustomerId(customerId);
    }

    @GetMapping("/tags/{tagType}")
    public Response getTagsByTagTypeId(@PathVariable(name="tagType") String tagType) throws DataNotFoundException {
        return paymentService.getTagsByTagType(tagType);
    }
}
