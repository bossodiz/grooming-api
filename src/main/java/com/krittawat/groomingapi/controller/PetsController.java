package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.request.PetRequest;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.BadRequestException;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.PetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetsController {

    private final PetsService petsService;


    @PostMapping("/add")
    public Response add(@RequestBody PetRequest request) throws DataNotFoundException, BadRequestException {
        return petsService.add(request);
    }
}
