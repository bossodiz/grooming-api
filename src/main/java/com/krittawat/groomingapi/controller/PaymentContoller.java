package com.krittawat.groomingapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.krittawat.groomingapi.controller.request.CalculateRequest;
import com.krittawat.groomingapi.controller.request.GenerateQrRequest;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.PaymentService;
import jakarta.validation.Valid;
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

    @GetMapping("/grooming-services")
    public Response getGroomingServices(@RequestParam (name = "petTypeId", required = true) Long petTypeId) throws DataNotFoundException {
        return paymentService.getGroomingServices(petTypeId);
    }

    @GetMapping("/pet-shop-services")
    public Response getPetShopProduct()  {
            return paymentService.getPetShopProduct();
    }

    @PostMapping("/calculate")
    public Response calculate(
            @RequestBody CalculateRequest request
    ) throws DataNotFoundException, JsonProcessingException {
        return paymentService.calculate(request);
    }

    @PostMapping("/generate-qr")
    public Response generateQr(@Valid @RequestBody GenerateQrRequest request) throws Exception {
        return paymentService.generateQrCode(request);
    }


}
