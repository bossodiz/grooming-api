package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.GroomingServiceRequest;
import com.krittawat.groomingapi.controller.response.GroomingServiceResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EGroomingService;
import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.service.GroomingServiceService;
import com.krittawat.groomingapi.datasource.service.PetTypeService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroomingService {

    private final GroomingServiceService groomingServiceService;
    private final PetTypeService petTypeService;

    public Response getList(String type) throws DataNotFoundException {
        EPetType petType = petTypeService.getByName(type);
        List<EGroomingService> list = groomingServiceService.getGroomingServiceByPetTypeId(petType.getId()).stream()
                .sorted(Comparator.comparing((EGroomingService e) -> e.getPetType().getId())
                        .thenComparing(EGroomingService::getSequence))
                .toList();
        List<GroomingServiceResponse> responsesList = new ArrayList<>();
        list.forEach(item -> {
            GroomingServiceResponse obj = GroomingServiceResponse.builder()
                    .id(item.getId())
                    .nameTh(item.getNameTh())
                    .nameEn(item.getNameEn())
                    .typeTh(item.getPetType().getNameTh())
                    .typeEn(item.getPetType().getNameEn())
                    .price(UtilService.toString(item.getPrice()))
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
        EGroomingService groomingService = groomingServiceService.getByIdOrNew(request.getId());
        groomingService.setNameTh(request.getNameTh());
        groomingService.setNameEn(request.getNameEn());
        groomingService.setPrice(UtilService.toBigDecimal(request.getPrice()));
        groomingService.setRemark(request.getRemark());
        if (groomingService.getId() == null) {
            groomingService.setSequence(groomingServiceService.getSequenceByPetTypeId(request.getPetTypeId()));
        }
        groomingService.setPetType(petTypeService.getById(request.getPetTypeId()));
        groomingService.setRemark(request.getRemark());
        groomingServiceService.save(groomingService);
        return Response.builder()
                .code(200)
                .message("Grooming service added successfully")
                .build();
    }

    public Response getById(Long id) throws DataNotFoundException {
        EGroomingService groomingService = groomingServiceService.getById(id);
        GroomingServiceResponse response = GroomingServiceResponse.builder()
                .id(groomingService.getId())
                .nameTh(groomingService.getNameTh())
                .nameEn(groomingService.getNameEn())
                .typeId(groomingService.getPetType().getId())
                .price(UtilService.toString(groomingService.getPrice()))
                .remark(groomingService.getRemark())
                .build();
        return Response.builder()
                .code(200)
                .message("Grooming service found")
                .data(response)
                .build();
    }

    public Response deleteGroomingService(Long id) throws DataNotFoundException {
        groomingServiceService.deleteById(id);
        return Response.builder()
                .code(200)
                .message("Grooming service deleted successfully")
                .build();
    }

}
