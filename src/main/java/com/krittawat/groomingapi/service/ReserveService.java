package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.ReserveRequest;
import com.krittawat.groomingapi.controller.response.ReserveGroomingResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EGroomingReserve;
import com.krittawat.groomingapi.datasource.entity.EPet;
import com.krittawat.groomingapi.datasource.service.GroomingReserveService;
import com.krittawat.groomingapi.datasource.service.PetBreedService;
import com.krittawat.groomingapi.datasource.service.PetService;
import com.krittawat.groomingapi.datasource.service.PetTypeService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final GroomingReserveService groomingReserveService;
    private final PetService petService;
    private final PetTypeService petTypeService;
    private final PetBreedService petBreedService;

    public Response getReserveGrooming(String start, String end) {
        LocalDateTime startDateTime = start != null ? LocalDateTime.parse(start) : LocalDateTime.now().minusMonths(1);
        LocalDateTime endDateTime = end != null ? LocalDateTime.parse(end) : LocalDateTime.now().plusMonths(1);
        List<EGroomingReserve> list = groomingReserveService.getByStartEnd(startDateTime, endDateTime);
        return Response.builder()
                .code(200)
                .message("OK")
                .data(list.stream()
                        .map(item-> ReserveGroomingResponse.builder()
                                .className(item.getColor())
                                .title(EnumUtil.RESERVATION_TYPE.GROOMING.name())
                                .start(item.getReserveDateStart())
                                .end(item.getReserveDateEnd())
                                .extendedProps(ReserveGroomingResponse.ExtendedProps.builder()
                                        .id(item.getId())
                                        .pet(item.getPet() != null ? item.getPet().getId() : null)
                                        .petName(item.getPet() != null ? item.getPet().getName() : item.getPetName())
                                        .phone(item.getPhone())
                                        .petType(item.getPetType() != null ? item.getPetType().getId() : null)
                                        .petBreed(item.getPetBreed() != null ? item.getPetBreed().getId() : null)
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
        eGroomingReserve.setPhone(request.getPhone());
        eGroomingReserve.setColor(request.getColor());
        eGroomingReserve.setNote(request.getNote());
        eGroomingReserve.setReserveDateStart(request.getStart());
        eGroomingReserve.setReserveDateEnd(request.getEnd());
        eGroomingReserve = groomingReserveService.save(eGroomingReserve);
        return Response.builder()
                .code(200)
                .message("OK")
                .data(ReserveGroomingResponse.builder()
                        .className(eGroomingReserve.getColor())
                        .title(EnumUtil.RESERVATION_TYPE.GROOMING.name())
                        .start(eGroomingReserve.getReserveDateStart())
                        .end(eGroomingReserve.getReserveDateEnd())
                        .extendedProps(ReserveGroomingResponse.ExtendedProps.builder()
                                .id(eGroomingReserve.getId())
                                .pet(eGroomingReserve.getPet() != null ? eGroomingReserve.getPet().getId() : null)
                                .petName(eGroomingReserve.getPet() != null ? eGroomingReserve.getPet().getName() : eGroomingReserve.getPetName())
                                .phone(eGroomingReserve.getPhone())
                                .petType(eGroomingReserve.getPetType() != null ? eGroomingReserve.getPetType().getId() : null)
                                .petBreed(eGroomingReserve.getPetBreed() != null ? eGroomingReserve.getPetBreed().getId() : null)
                                .note(eGroomingReserve.getNote())
                                .build())
                        .build())
                .build();
    }


    public Response updateReserveGrooming(ReserveRequest request) {
        EGroomingReserve eGroomingReserve = groomingReserveService.getById(request.getId());
        eGroomingReserve.setReserveDateStart(request.getStart());
        eGroomingReserve.setReserveDateEnd(request.getEnd());
        eGroomingReserve = groomingReserveService.save(eGroomingReserve);
        return Response.builder()
                .code(200)
                .message("OK")
                .data(ReserveGroomingResponse.builder()
                        .className(eGroomingReserve.getColor())
                        .title(EnumUtil.RESERVATION_TYPE.GROOMING.name())
                        .start(eGroomingReserve.getReserveDateStart())
                        .end(eGroomingReserve.getReserveDateEnd())
                        .extendedProps(ReserveGroomingResponse.ExtendedProps.builder()
                                .id(eGroomingReserve.getId())
                                .pet(eGroomingReserve.getPet() != null ? eGroomingReserve.getPet().getId() : null)
                                .petName(eGroomingReserve.getPet() != null ? eGroomingReserve.getPet().getName() : eGroomingReserve.getPetName())
                                .phone(eGroomingReserve.getPhone())
                                .petType(eGroomingReserve.getPetType() != null ? eGroomingReserve.getPetType().getId() : null)
                                .petBreed(eGroomingReserve.getPetBreed() != null ? eGroomingReserve.getPetBreed().getId() : null)
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
