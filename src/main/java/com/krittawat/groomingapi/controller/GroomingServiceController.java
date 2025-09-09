package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.request.GroomingServiceRequest;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.GroomingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/grooming-service")
@RequiredArgsConstructor
public class GroomingServiceController {

    private final GroomingService groomingService;

    @GetMapping("")
    public Response getById(@RequestParam(name="id") Long id) throws DataNotFoundException {
        return groomingService.getById(id);
    }

    @GetMapping("/list")
    public Response getList() {
        return groomingService.getList();
    }

    @PostMapping("/save")
    public Response addGroomingService(@RequestBody GroomingServiceRequest request) throws DataNotFoundException {
        return groomingService.saveGroomingService(request);
    }

    @DeleteMapping("/delete")
    public Response deleteGroomingService(@RequestParam(name="id") Long id) throws DataNotFoundException {
        return groomingService.deleteGroomingService(id);
    }

}
