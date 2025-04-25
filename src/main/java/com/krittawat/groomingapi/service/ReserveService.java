package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.ReserveRequest;
import com.krittawat.groomingapi.controller.response.ReserveGroomingResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EGroomingReserve;
import com.krittawat.groomingapi.datasource.entity.EPet;
import com.krittawat.groomingapi.datasource.service.*;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final GroomingReserveService groomingReserveService;
    private final GroomingServiceService groomingServiceService;
    private final PetService petService;
    private final PetTypeService petTypeService;
    private final PetBreedService petBreedService;

    public Response getReserveGrooming() {
        List<EGroomingReserve> list = groomingReserveService.getAll();
        return Response.builder()
                .code(200)
                .message("OK")
                .data(list.stream()
                        .map(item-> ReserveGroomingResponse.builder()
                                .className(item.getColor())
                                .title(EnumUtil.RESERVATION_TYPE.GROOMING.name())
                                .start(item.getReserveDateStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                                .end(item.getReserveDateEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                                .extendedProps(ReserveGroomingResponse.ExtendedProps.builder()
                                        .id(item.getId())
                                        .pet(item.getPet() != null ? item.getPet().getId() : null)
                                        .petName(item.getPet() != null ? item.getPet().getName() : item.getPetName() != null ? item.getPetName() : null)
                                        .phone(item.getPhone())
                                        .petType(item.getPetType() != null ? item.getPetType().getId() : null)
                                        .petBreed(item.getPetBreed() != null ? item.getPetBreed().getId() : null)
                                        .serviceId(item.getGroomingService() != null ? item.getGroomingService().getId() : null)
                                        .serviceName(item.getGroomingService() != null ? item.getGroomingService().getNameTh() : null)
                                        .note(item.getNote())
                                        .build())
                                .build())
                        .toList())
                .build();
    }


    public Response reserveGrooming(ReserveRequest request) throws DataNotFoundException {
        EGroomingReserve eGroomingReserve = null;
        if (request.getId() != null) {
            eGroomingReserve = groomingReserveService.getById(request.getId());
        }
        if (eGroomingReserve == null){
            eGroomingReserve = new EGroomingReserve();
        }
        if (request.getPet() != null) {
            EPet pet = petService.findById(request.getPet()).orElseThrow(() -> new DataNotFoundException("pet not found"));
            eGroomingReserve.setPet(pet);
        } else {
            eGroomingReserve.setPetName(request.getNameOther());
        }
        if (request.getType() != null){
            eGroomingReserve.setPetType(petTypeService.getById(request.getType()));
        }
        if (request.getBreed() != null){
            eGroomingReserve.setPetBreed(petBreedService.getById(request.getBreed()));
        }
        if (request.getGrooming() != null){
            eGroomingReserve.setGroomingService(groomingServiceService.getById(request.getGrooming()));
        }
        eGroomingReserve.setPhone(request.getPhone());
        eGroomingReserve.setColor(request.getColor());
        eGroomingReserve.setNote(request.getNote());
        eGroomingReserve.setReserveDateStart(LocalDateTime.parse(request.getStart()));
        eGroomingReserve.setReserveDateEnd(LocalDateTime.parse(request.getEnd()));
        eGroomingReserve = groomingReserveService.save(eGroomingReserve);
        return Response.builder()
                .code(200)
                .message("OK")
                .data(ReserveGroomingResponse.builder()
                        .className(eGroomingReserve.getColor())
                        .title(EnumUtil.RESERVATION_TYPE.GROOMING.name())
                        .start(eGroomingReserve.getReserveDateStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .end(eGroomingReserve.getReserveDateEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .extendedProps(ReserveGroomingResponse.ExtendedProps.builder()
                                .id(eGroomingReserve.getId())
                                .pet(eGroomingReserve.getPet() != null ? eGroomingReserve.getPet().getId() : null)
                                .petName(eGroomingReserve.getPet() != null ? eGroomingReserve.getPet().getName() : eGroomingReserve.getPetName() != null ? eGroomingReserve.getPetName() : null)
                                .phone(eGroomingReserve.getPhone())
                                .petType(eGroomingReserve.getPetType() != null ? eGroomingReserve.getPetType().getId() : null)
                                .petBreed(eGroomingReserve.getPetBreed() != null ? eGroomingReserve.getPetBreed().getId() : null)
                                .serviceId(eGroomingReserve.getGroomingService().getId())
                                .serviceName(eGroomingReserve.getGroomingService().getNameTh())
                                .note(eGroomingReserve.getNote())
                                .build())
                        .build())
                .build();
    }


    public Response updateReserveGrooming(ReserveRequest request) {
        EGroomingReserve eGroomingReserve = groomingReserveService.getById(request.getId());
        eGroomingReserve.setReserveDateStart(LocalDateTime.parse(request.getStart()));
        eGroomingReserve.setReserveDateEnd(LocalDateTime.parse(request.getEnd()));
        eGroomingReserve = groomingReserveService.save(eGroomingReserve);
        return Response.builder()
                .code(200)
                .message("OK")
                .data(ReserveGroomingResponse.builder()
                        .className(eGroomingReserve.getColor())
                        .title(EnumUtil.RESERVATION_TYPE.GROOMING.name())
                        .start(eGroomingReserve.getReserveDateStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .end(eGroomingReserve.getReserveDateEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .extendedProps(ReserveGroomingResponse.ExtendedProps.builder()
                                .id(eGroomingReserve.getId())
                                .pet(eGroomingReserve.getPet() != null ? eGroomingReserve.getPet().getId() : null)
                                .petName(eGroomingReserve.getPet() != null ? eGroomingReserve.getPet().getName() : eGroomingReserve.getPetName() != null ? eGroomingReserve.getPetName() : null)
                                .phone(eGroomingReserve.getPhone())
                                .petType(eGroomingReserve.getPetType() != null ? eGroomingReserve.getPetType().getId() : null)
                                .petBreed(eGroomingReserve.getPetBreed() != null ? eGroomingReserve.getPetBreed().getId() : null)
                                .serviceId(eGroomingReserve.getGroomingService().getId())
                                .serviceName(eGroomingReserve.getGroomingService().getNameTh())
                                .note(eGroomingReserve.getNote())
                                .build())
                        .build())
                .build();
    }

    public Response deleteReserveGrooming(Long id) {
        groomingReserveService.deleteById(id);
        return Response.builder()
                .code(200)
                .message("OK")
                .build();
    }
}
