package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.GroomingServiceRequest;
import com.krittawat.groomingapi.controller.response.GroomingServiceResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.service.ItemsService;
import com.krittawat.groomingapi.datasource.service.PetTypeService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroomingService {

    private final ItemsService itemsService;
    private final PetTypeService petTypeService;

    public Response getList() {
        List<EItem> list = itemsService.getGroomingService().stream()
                .sorted(Comparator.comparing(EItem::getName)).toList();
        List<GroomingServiceResponse> responsesList = new ArrayList<>();
        list.forEach(item -> {
            GroomingServiceResponse obj = GroomingServiceResponse.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .typeName(UtilService.getEnumName(item.getItemCategory()))
                    .price(UtilService.toString(item.getPrice(), 0))
                    .description(item.getDescription())
                    .remark(item.getRemark())
                    .build();
            responsesList.add(obj);
        });
        return Response.builder()
                .code(200)
                .message("Grooming service found")
                .data(responsesList)
                .build();
    }

    public Response saveGroomingService(GroomingServiceRequest request) throws DataNotFoundException {
        EPetType petType = petTypeService.getById(request.getType());
        EItem item = itemsService.getByIdOrNew(request.getId());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(UtilService.toBigDecimal(request.getPrice()));
        item.setItemCategory(EnumUtil.ITEM_CATEGORY.valueOf(petType.getName().toUpperCase()));
        item.setRemark(request.getRemark());
        item.setBarcode(request.getBarcode());
        itemsService.save(item);
        return Response.builder()
                .code(200)
                .message("Grooming service added successfully")
                .data(item)
                .build();
    }

    public Response getById(Long id) throws DataNotFoundException {
        EItem groomingService = itemsService.getById(id);
        String type = groomingService.getItemCategory().name();
        EPetType petType = petTypeService.getByName(type.toLowerCase());
        GroomingServiceResponse response = GroomingServiceResponse.builder()
                .id(groomingService.getId())
                .name(groomingService.getName())
                .description(groomingService.getDescription())
                .type(petType.getId())
                .price(UtilService.toString(groomingService.getPrice()))
                .remark(groomingService.getRemark())
                .barcode(groomingService.getBarcode())
                .build();
        return Response.builder()
                .code(200)
                .message("Grooming service found")
                .data(response)
                .build();
    }

    public Response deleteGroomingService(Long id) throws DataNotFoundException {
        itemsService.deleteById(id);
        return Response.builder()
                .code(200)
                .message("Grooming service deleted successfully")
                .build();
    }

}
