package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.CustomerRequest;
import com.krittawat.groomingapi.controller.request.RegisterRequest;
import com.krittawat.groomingapi.controller.response.CustomerResponse;
import com.krittawat.groomingapi.controller.response.PetResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EPet;
import com.krittawat.groomingapi.datasource.entity.EUserProfile;
import com.krittawat.groomingapi.datasource.service.PetService;
import com.krittawat.groomingapi.datasource.service.RoleService;
import com.krittawat.groomingapi.datasource.service.UserService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserService userService;
    private final RoleService roleService;
    private final PetService petService;

    @Transactional
    public Response register(RegisterRequest request) throws DataNotFoundException {
        String a = "";
        if (userService.existsByUsername(request.getUsername().trim())) {
            EUserProfile user = userService.findByUsername(request.getUsername().trim());
            return Response.builder()
                    .code(200)
                    .message("Phone is already taken")
                    .data(CustomerResponse.builder()
                            .id(user.getId())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .name(user.getNickname())
                            .phone(user.getPhone1())
                            .phoneOther(user.getPhone2())
                            .build())
                    .build();
        }
        EUserProfile user = new EUserProfile();
        user.setUsername(UtilService.trimOrNull(request.getUsername()));
        user.setPassword("");
        user.setFirstname(UtilService.trimOrNull(request.getFirstname()));
        user.setLastname(UtilService.trimOrNull(request.getLastname()));
        user.setNickname(UtilService.trimOrNull(request.getNickname()));
        user.setEmail(UtilService.trimOrNull(request.getEmail()));
        user.setPhone1(UtilService.trimOrNull(request.getUsername()));
        user.setPhone2(UtilService.trimOrNull(request.getPhone2()));
        user.setRole(roleService.getRoleCUSTOMER());
        user.setVisit(0);
        user = userService.save(user);
        return Response.builder()
                .code(200)
                .message("User registered successfully")
                .data(CustomerResponse.builder()
                        .id(user.getId())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .name(user.getNickname())
                        .phone(user.getPhone1())
                        .phoneOther(user.getPhone2())
                        .build())
                .build();
    }

    public Response getCustomers() throws DataNotFoundException {
        List<EUserProfile> list = userService.findByCustomers();
        return Response.builder()
                .code(200)
                .message("Customers found")
                .data(list.stream().map(user -> CustomerResponse.builder()
                        .id(user.getId())
                        .name(user.getNickname())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .phone(user.getUsername())
                        .phoneOther(user.getPhone2())
                        .serviceCount(user.getVisit())
                        .createdDate(user.getCreatedDate())
                        .lastedDate(user.getLastedDate())
                        .remark(user.getRemark())
                        .pets(List.of(user.getPets().stream().map(pet -> PetResponse.builder()
                                        .id(pet.getId())
                                        .build())
                                .toArray(PetResponse[]::new)))
                        .build())
                        .toArray(CustomerResponse[]::new))
                .build();

    }

    public Response getCustomersById(Long id) throws DataNotFoundException {
        EUserProfile user = userService.findByCustomersId(id);
        List<EPet> pets = petService.findByUser(user);
        return Response.builder()
                .code(200)
                .message("Customer found")
                .data(CustomerResponse.builder()
                        .id(user.getId())
                        .name(user.getNickname())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .phone(user.getUsername())
                        .phoneOther(user.getPhone2())
                        .email(user.getEmail())
                        .serviceCount(user.getVisit())
                        .createdDate(user.getCreatedDate())
                        .lastedDate(user.getLastedDate())
                        .remark(user.getRemark())
                        .pets(List.of(pets.stream().map(pet -> PetResponse.builder()
                                        .id(pet.getId())
                                        .build())
                                .toArray(PetResponse[]::new)))
                        .build())
                .build();
    }

    public Response updateCustomersById(Long id, CustomerRequest request) throws DataNotFoundException {
        EUserProfile user = userService.findByCustomersId(id);
        if (userService.existsByUsernameNotId(id, request.getPhone())){
            return Response.builder()
                    .code(400)
                    .message("Phone is already taken")
                    .build();

        }
        user.setFirstname(UtilService.trimOrNull(request.getFirstname()));
        user.setLastname(UtilService.trimOrNull(request.getLastname()));
        user.setNickname(UtilService.trimOrNull(request.getName()));
        user.setEmail(UtilService.trimOrNull(request.getEmail()));
        user.setPhone1(UtilService.trimOrNull(request.getPhone()));
        user.setPhone2(UtilService.trimOrNull(request.getPhone2()));
        user.setUsername(UtilService.trimOrNull(request.getPhone()));
        user = userService.save(user);
        return Response.builder()
                .code(200)
                .message("Customer updated successfully")
                .data(CustomerResponse.builder()
                        .id(user.getId())
                        .name(user.getNickname())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .phone(user.getUsername())
                        .phoneOther(user.getPhone2())
                        .email(user.getEmail())
                        .build())
                .build();
    }

    public Response updateCustomersRemarkById(Long id, CustomerRequest request) throws DataNotFoundException {
        EUserProfile user = userService.findByCustomersId(id);
        user.setRemark(UtilService.trimOrNull(request.getRemark()));
        user = userService.save(user);
        return Response.builder()
                .code(200)
                .message("Customer updated successfully")
                .data(CustomerResponse.builder()
                        .remark(user.getRemark())
                        .build())
                .build();
    }

    public Response getCustomersRemarkById(Long id) throws DataNotFoundException {
        EUserProfile user = userService.findByCustomersId(id);
        return Response.builder()
                .code(200)
                .message("Customers found")
                .data(CustomerResponse.builder()
                    .id(user.getId())
                    .remark(user.getRemark())
                    .build())
                .build();
    }

    public Response getPetsByCustomerId(Long id) throws DataNotFoundException {
        EUserProfile user = userService.findByCustomersId(id);
        List<EPet> pets = petService.findByUser(user);
        return Response.builder()
                .code(200)
                .message("Pets found")
                .data(pets.stream().map(pet -> {
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
                        }).toList())
                .build();
    }
}
