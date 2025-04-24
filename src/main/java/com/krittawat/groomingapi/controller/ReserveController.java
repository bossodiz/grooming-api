package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.request.ReserveRequest;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReserveController {
    private final ReserveService reserveService;

    @GetMapping("/grooming")
    public Response grooming() {
        return reserveService.getReserveGrooming();
    }

    @PostMapping("/grooming")
    public Response reserveGrooming(@RequestBody ReserveRequest request) throws DataNotFoundException {
        return reserveService.reserveGrooming(request);
    }
}
