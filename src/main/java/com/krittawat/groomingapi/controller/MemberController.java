package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.request.CustomerRequest;
import com.krittawat.groomingapi.controller.request.RegisterRequest;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.BadRequestException;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public Response register(@RequestBody RegisterRequest request) throws DataNotFoundException, BadRequestException {
        return memberService.register(request);
    }

    @GetMapping("/customers")
    public Response getCustomers() throws DataNotFoundException {
        return memberService.getCustomers();
    }

    @GetMapping("/customers/{id}")
    public Response getCustomersById(@PathVariable Long id) throws DataNotFoundException {
        return memberService.getCustomersById(id);
    }

    @PatchMapping("/customers/{id}")
    public Response updateCustomersById(@PathVariable Long id, @RequestBody CustomerRequest request) throws DataNotFoundException, BadRequestException {
        return memberService.updateCustomersById(id, request);
    }

    @PatchMapping("/customers/remark/{id}")
    public Response updateCustomersRemarkById(@PathVariable Long id, @RequestBody CustomerRequest request) throws DataNotFoundException, BadRequestException {
        return memberService.updateCustomersRemarkById(id, request);
    }

    @GetMapping("/customers/remark/{id}")
    public Response getCustomersRemarkById(@PathVariable Long id) throws DataNotFoundException {
        return memberService.getCustomersRemarkById(id);
    }

    @GetMapping("/customers/{id}/pets")
    public Response getPetsByCustomerId(@PathVariable Long id) throws DataNotFoundException {
        return memberService.getPetsByCustomerId(id);
    }
}
