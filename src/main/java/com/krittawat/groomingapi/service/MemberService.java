package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.RegisterRequest;
import com.krittawat.groomingapi.controller.response.CustomerResponse;
import com.krittawat.groomingapi.controller.response.RegisterResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.entity.EUser;
import com.krittawat.groomingapi.datasource.service.RoleService;
import com.krittawat.groomingapi.datasource.service.UserService;
import com.krittawat.groomingapi.error.BadRequestException;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.model.RoleProfile;
import com.krittawat.groomingapi.service.model.UserProfile;
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

    @Transactional
    public Response register(RegisterRequest request) throws BadRequestException, DataNotFoundException {
        if (userService.existsByUsername(request.getUsername().trim())) {
            EUser user = userService.findByUsername(request.getUsername().trim());
            return Response.builder()
                    .code(200)
                    .message("Phone is already taken")
                    .data(RegisterResponse.builder()
                            .id(user.getId())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .nickname(user.getNickname())
                            .email(user.getEmail())
                            .phone1(user.getPhone1())
                            .phone2(user.getPhone2())
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
        user.setServiceCount(0);
        user = userService.save(user);
        return Response.builder()
                .code(200)
                .message("User registered successfully")
                .data(RegisterResponse.builder()
                        .id(user.getId())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .phone1(user.getPhone1())
                        .phone2(user.getPhone2())
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
                        .serviceCount(user.getServiceCount())
                        .createdDate(user.getCreatedDate())
                        .lastedDate(user.getLastedDate())
                        .build())
                        .toArray(CustomerResponse[]::new))
                .build();

    }
}
