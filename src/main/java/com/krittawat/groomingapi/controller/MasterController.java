package com.krittawat.groomingapi.controller;

import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.MasterService;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/pet-list")
    public Response getPetList() {
        return masterService.getPetList();
    }

    @GetMapping("/pet-type")
    public Response getPetTypeByName(@RequestParam(name = "name") String name) throws DataNotFoundException {
        return masterService.getPetTypeByName(name);
    }

    @GetMapping("/tags")
    public Response getTagsByType(@RequestParam(name = "type") EnumUtil.ITEM_TYPE type) throws DataNotFoundException {
        return masterService.getTagsByType(type);
    }

}
