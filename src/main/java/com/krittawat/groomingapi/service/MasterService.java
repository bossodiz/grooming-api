package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.response.DropdownResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.controller.response.TagResponse;
import com.krittawat.groomingapi.datasource.entity.*;
import com.krittawat.groomingapi.datasource.service.*;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterService {

    private final PetTypeService petTypeService;
    private final PetBreedService petBreedService;
    private final PetService petService;
    private final ItemsService itemsService;
    private final TagService tagService;

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

    public Response getPetList() {
        List<EPet> list = petService.findAll();
        return Response.builder()
                .code(200)
                .message("Pet found")
                .data(list.stream()
                        .map(item -> DropdownResponse.builder()
                                .key(item.getId())
                                .ref_key(item.getTypeId())
                                .ref_key2(item.getBreedId())
                                .ref_key3(item.getUser().getPhone1())
                                .ref_key4(item.getUser().getNickname())
                                .value_th(item.getName())
                                .value_en(item.getName())
                                .build())
                        .toList())
                .build();
    }

    public Response getPetTypeByName(String name) throws DataNotFoundException {
        EPetType petType = petTypeService.getByName(name);
        return Response.builder()
                .code(200)
                .message("Pet type found")
                .data(DropdownResponse.builder()
                        .key(petType.getId())
                        .value_th(petType.getNameTh())
                        .value_en(petType.getNameEn())
                        .build())
                .build();
    }

    public Response getTagsByType(EnumUtil.ITEM_TYPE type) {
        List<EItem> list = itemsService.getItemsByType(type);
        Set<ETag> tags = list.stream()
                .flatMap(item -> item.getTags().stream())
                .collect(Collectors.toSet());
        return Response.builder()
                .code(200)
                .message("Tags found")
                .data(tags.stream()
                        .map(item -> TagResponse.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .build())
                        .toList())
                .build();
    }
}
