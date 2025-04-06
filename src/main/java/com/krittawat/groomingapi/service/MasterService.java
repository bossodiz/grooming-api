package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.response.DropdownResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EPetBreed;
import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.service.PetBreedService;
import com.krittawat.groomingapi.datasource.service.PetTypeService;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterService {

    private final PetTypeService petTypeService;
    private final PetBreedService petBreedService;



    public Response getPetType() {
        List<EPetType> list = petTypeService.getAll();
        return Response.builder()
                .code(200)
                .message("Pet type found")
                .data(list.stream()
                    .map(item -> DropdownResponse.builder()
                            .key(item.getId())
                            .value_th(item.getNameTh())
                            .value_en(item.getNameEn())
                            .build())
                    .toList())
                .build();
    }

    public Response getPetBreed() {
        List<EPetBreed> list = petBreedService.getAll();
        return Response.builder()
                .code(200)
                .message("Pet breed found")
                .data(list.stream()
                        .map(item -> DropdownResponse.builder()
                                .key(item.getId())
                                .ref_key(item.getPetType().getId())
                                .value_th(item.getNameTh())
                                .value_en(item.getNameEn())
                                .image_url(item.getImage())
                                .build())
                        .toList())
                .build();
    }
}
