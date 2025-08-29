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
    public Response grooming(@RequestParam(name = "start" , required = false) String start,
                             @RequestParam(name = "end", required = false) String end) {
        return reserveService.getReserveGrooming(start, end);
    }

    @PostMapping("/grooming")
    public Response reserveGrooming(@RequestBody ReserveRequest request) throws DataNotFoundException {
        return reserveService.reserveGrooming(request);
    }

    @PutMapping("/grooming")
    public Response updateReserveGrooming(@RequestBody ReserveRequest request) throws DataNotFoundException {
        return reserveService.updateReserveGrooming(request);
    }

    @DeleteMapping("/grooming/{id}")
    public Response deleteReserveGrooming(@PathVariable Long id) {
        return reserveService.deleteReserveGrooming(id);
    }
}
