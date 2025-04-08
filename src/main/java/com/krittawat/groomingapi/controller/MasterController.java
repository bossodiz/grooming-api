package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.service.MasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/master")
@RequiredArgsConstructor
public class MasterController {
    private final MasterService masterService;

    @GetMapping("/pet-types")
    public Response getPetType() {
        return masterService.getPetType();
    }

    @GetMapping("/pet-breeds")
    public Response getPetBreed() {
        return masterService.getPetBreed();
    }

}
