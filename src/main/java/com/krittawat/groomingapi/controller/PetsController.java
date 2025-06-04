package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.request.PetRequest;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.PetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetsController {

    private final PetsService petsService;

    @GetMapping("/list")
    public Response getCustomers() {
        return petsService.getPets();
    }

    @PostMapping("/add")
    public Response add(@RequestBody PetRequest request) throws DataNotFoundException {
        return petsService.add(request);
    }

    @GetMapping("/{id}")
    public Response getPetById(@PathVariable Long id) throws DataNotFoundException {
        return petsService.getPetById(id);
    }


    @PutMapping("/update")
    public Response update(@RequestBody PetRequest request) throws DataNotFoundException {
        return petsService.update(request);
    }
}
