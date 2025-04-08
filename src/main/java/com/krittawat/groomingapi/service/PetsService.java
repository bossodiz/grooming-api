package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.PetRequest;
import com.krittawat.groomingapi.controller.response.PetResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EPet;
import com.krittawat.groomingapi.datasource.entity.EPetBreed;
import com.krittawat.groomingapi.datasource.entity.EUser;
import com.krittawat.groomingapi.datasource.service.PetService;
import com.krittawat.groomingapi.datasource.service.UserService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetsService {
    private final PetService petService;
    private final UserService userService;

    public Response add(PetRequest request) throws DataNotFoundException {
        EUser user = userService.findByCustomersId(request.getCustomerId());
        List<EPet> pets = petService.findByUserAndName(user, request.getName());
        if (!pets.isEmpty()){
            throw new DataNotFoundException("Pet already exist");
        }
        EPetBreed breed = petService.findBreedById(request.getBreed())
                .orElseThrow(() -> new DataNotFoundException("Breed not found"));
        EPet pet = new EPet();
        pet.setName(request.getName());
        pet.setAgeMonth(request.getAgeMonth());
        pet.setAgeYear(request.getAgeYear());
        pet.setGender(request.getGender());
        pet.setPetBreed(breed);
        pet.setServiceCount(0);
        pet.setUser(user);
        pet = petService.insert(pet);
        return Response.builder()
                .code(200)
                .message("Pet added successfully")
                .data(PetResponse.builder()
                        .id(pet.getId())
                        .name(pet.getName())
                        .ageYear(pet.getAgeYear())
                        .ageMonth(pet.getAgeMonth())
                        .gender(pet.getGender().name())
                        .breedNameTh(pet.getPetBreed().getNameTh())
                        .breedNameEn(pet.getPetBreed().getNameEn())
                        .typeNameTh(pet.getPetBreed().getPetType().getNameTh())
                        .typeNameEn(pet.getPetBreed().getPetType().getNameEn())
                        .breedId(pet.getPetBreed().getId())
                        .typeId(pet.getPetBreed().getPetType().getId())
                ).build();
    }
}
