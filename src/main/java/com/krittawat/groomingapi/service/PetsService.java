package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.PetRequest;
import com.krittawat.groomingapi.controller.response.PetResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EPet;
import com.krittawat.groomingapi.datasource.entity.EPetBreed;
import com.krittawat.groomingapi.datasource.entity.EPetType;
import com.krittawat.groomingapi.datasource.entity.EUserProfile;
import com.krittawat.groomingapi.datasource.service.PetService;
import com.krittawat.groomingapi.datasource.service.UserService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetsService {
    private final PetService petService;
    private final UserService userService;

    public Response add(PetRequest request) throws DataNotFoundException {
        EUserProfile user = userService.findByCustomersId(request.getCustomerId());
        List<EPet> pets = petService.findByUserAndName(user, request.getName());
        if (!pets.isEmpty()){
            throw new DataNotFoundException("Pet already exist");
        }
        EPet pet = new EPet();
        if (request.getBreed() != null && request.getBreed() != 0L) {
            EPetBreed breed = petService.findBreedById(request.getBreed())
                    .orElseThrow(() -> new DataNotFoundException("Breed not found"));
            pet.setPetBreed(breed);
        }
        if (request.getType() != null && request.getType() != 0L) {
            EPetType type = petService.findTypeById(request.getType())
                    .orElseThrow(() -> new DataNotFoundException("Type not found"));
            pet.setPetType(type);
        }
        pet.setName(request.getName());
        pet.setAgeMonth(request.getAgeMonth() == null ? 0 : request.getAgeMonth());
        pet.setAgeYear(request.getAgeYear() == null ? 0 : request.getAgeYear());
        pet.setLastUpdateYear(LocalDateTime.now());
        pet.setGender(request.getGender());
        pet.setWeight(UtilService.toBigDecimal(request.getWeight()));
        pet.setServiceCount(0);
        pet.setUser(user);
        petService.save(pet);
        return Response.builder()
                .code(200)
                .message("Pet added successfully")
                .build();
    }

    public Response getPets() {
        List<EPet> list = petService.findAll();
        List<PetResponse> pets = list.stream()
            .map(pet -> {
                EPet.Age age = pet.computeCurrentAge();
                return PetResponse.builder()
                    .id(pet.getId())
                    .name(pet.getName())
                    .ageYear(age.years())
                    .ageMonth(age.months())
                    .gender(UtilService.getEnum(pet.getGender()))
                    .genderTh(UtilService.getNameThDefaulterDash(pet.getGender()))
                    .genderEn(UtilService.getNameEnDefaulterDash(pet.getGender()))
                    .breedNameTh(UtilService.getNameThDefaulterDash(pet::getPetBreed))
                    .breedNameEn(UtilService.getNameEnDefaulterDash(pet::getPetBreed))
                    .typeNameTh(UtilService.getNameThDefaulterDash(pet::getPetType))
                    .typeNameEn(UtilService.getNameEnDefaulterDash(pet::getPetType))
                    .breedId(pet.getBreedId())
                    .typeId(pet.getTypeId())
                    .weight(UtilService.toStringDefaulterDash(pet.getWeight()))
                    .service(UtilService.toStringDefaulterZero(pet.getServiceCount()))
                    .build();
            })
            .toList();
        return Response.builder()
                .code(200)
                .message("pets found")
                .data(pets)
                .build();
    }


    public Response getPetById(Long id) throws DataNotFoundException {
        EPet pet = petService.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Pet not found"));
        EPet.Age age = pet.computeCurrentAge();
        return Response.builder()
                .code(200)
                .message("Pet found")
                .data(PetResponse.builder()
                        .id(pet.getId())
                        .name(pet.getName())
                        .ageYear(age.years())
                        .ageMonth(age.months())
                        .gender(UtilService.getEnum(pet.getGender()))
                        .genderTh(UtilService.getNameThDefaulterDash(pet.getGender()))
                        .genderEn(UtilService.getNameEnDefaulterDash(pet.getGender()))
                        .breedId(pet.getBreedId())
                        .breedNameTh(UtilService.getNameThDefaulterDash(pet::getPetBreed))
                        .breedNameEn(UtilService.getNameEnDefaulterDash(pet::getPetBreed))
                        .typeId(pet.getTypeId())
                        .typeNameTh(UtilService.getNameThDefaulterDash(pet::getPetType))
                        .typeNameEn(UtilService.getNameEnDefaulterDash(pet::getPetType))
                        .weight(UtilService.toString(pet.getWeight()))
                        .service(UtilService.toStringDefaulterZero(pet.getServiceCount()))
                        .lastedServiceDate(LocalDateTime.now())
                        .customerId(pet.getUser().getId())
                        .customerName(pet.getUser().getNickname())
                        .customerPhone(pet.getUser().getPhone1())
                        .build()
                ).build();
    }

    public Response update(PetRequest request) throws DataNotFoundException {
        EPet pet = petService.findById(request.getId()).orElseThrow(() -> new DataNotFoundException("Pet not found"));
        if (request.getBreed() == null || request.getBreed() == 0L) {
            pet.setPetBreed(null);
        } else {
            EPetBreed breed = petService.findBreedById(request.getBreed())
                    .orElseThrow(() -> new DataNotFoundException("Breed not found"));
            pet.setPetBreed(breed);
        }
        if (request.getType() == null || request.getType() == 0L) {
            pet.setPetType(null);
        } else {
            EPetType type = petService.findTypeById(request.getType())
                    .orElseThrow(() -> new DataNotFoundException("Type not found"));
            pet.setPetType(type);
        }
        pet.setName(request.getName());
        pet.setAgeMonth(request.getAgeMonth() == null ? 0 : request.getAgeMonth());
        pet.setAgeYear(request.getAgeYear() == null ? 0 : request.getAgeYear());
        pet.setLastUpdateYear(LocalDateTime.now());
        pet.setGender(request.getGender());
        pet.setWeight(new BigDecimal(request.getWeight()));
        petService.save(pet);
        return Response.builder()
                .code(200)
                .message("Pet update successfully")
                .build();
    }
}
