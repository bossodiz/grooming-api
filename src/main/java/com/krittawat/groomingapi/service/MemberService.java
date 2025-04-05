package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.CustomerRequest;
import com.krittawat.groomingapi.controller.request.RegisterRequest;
import com.krittawat.groomingapi.controller.response.CustomerResponse;
import com.krittawat.groomingapi.controller.response.PetResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EPet;
import com.krittawat.groomingapi.datasource.entity.EUser;
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
        if (userService.existsByUsername(request.getUsername().trim())) {
            EUser user = userService.findByUsername(request.getUsername().trim());
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
        EUser user = new EUser();
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
        List<EUser> list = userService.findByCustomers();
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
                        .build())
                        .toArray(CustomerResponse[]::new))
                .build();

    }

    public Response getCustomersById(Long id) throws DataNotFoundException {
        EUser user = userService.findByCustomersId(id);
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
                        .pets(List.of(pets.stream().map(pet -> PetResponse.builder()
                                        .id(pet.getId())
                                        .name(pet.getName())
                                        .age(pet.getAge())
                                        .gender(pet.getGender().name())
                                        .breed(pet.getPetBreed().getName())
                                        .type(pet.getPetBreed().getPetType().getName())
                                        .weight(UtilService.toString(pet.getWeight()))
                                        .build())
                                .toArray(PetResponse[]::new)))
                        .build())
                .build();
    }

    public Response updateCustomersById(Long id, CustomerRequest request) throws DataNotFoundException {
        EUser user = userService.findByCustomersId(id);
        if (userService.existsByUsernameNotId(id, request.getPhone())){
            return Response.builder()
                    .code(400)
                    .message("Phone is already taken")
                    .build();

        };
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
}
