package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.response.PaymentCustomersResponse;
import com.krittawat.groomingapi.controller.response.PetResponse;
import com.krittawat.groomingapi.controller.response.Response;
import com.krittawat.groomingapi.datasource.service.UserService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserService userService; // Assuming you have a CustomerRepository to fetch customer data

    public Response getCustomers() throws DataNotFoundException {
        List<PaymentCustomersResponse> customerResponseList = new ArrayList<>(userService.findByCustomers().stream()
                .map(e -> {
                    String phone = Optional.ofNullable(e.getPhone1()).orElse("");
                    String phoneMask = phone.length() == 10
                            ? phone.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3")
                            : phone; // ถ้าไม่ใช่ 10 หลัก ไม่จัด format
                    String firstname = Optional.ofNullable(e.getFirstname()).orElse("");
                    String nickname = Optional.ofNullable(e.getNickname()).orElse("");
                    List<String> nameParts = new ArrayList<>();
                    if (!firstname.isEmpty()) nameParts.add(firstname);
                    if (!nickname.isEmpty()) nameParts.add(nickname);
                    String label = String.format("(%s)%s%s", phoneMask, nameParts.isEmpty() ? "" : " ", String.join(" - ", nameParts));
                    String name = String.format("%s%s", nameParts.isEmpty() ? "" : " ", String.join(" - ", nameParts));

                    List<PetResponse> pets = e.getPets().stream()
                            .map(pet -> PetResponse.builder()
                                    .id(pet.getId())
                                    .name(Optional.ofNullable(pet.getName()).orElse(""))
                                    .typeNameTh(Optional.ofNullable(pet.getPetType().getNameTh()).orElse(""))
                                    .typeNameEn(Optional.ofNullable(pet.getPetType().getNameEn()).orElse(""))
                                    .build())
                            .sorted(Comparator.comparing(PetResponse::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                            .toList();
                    return PaymentCustomersResponse.builder()
                            .key(e.getId())
                            .label(label)
                            .name(name)
                            .pets(pets)
                            .build();
                })
                .sorted(Comparator.comparing(PaymentCustomersResponse::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList());
        customerResponseList.add(0, PaymentCustomersResponse.builder()
                .key(null)
                .label("ไม่ระบุ")
                .name("No name")
                .pets(new ArrayList<>())
                .build()); // Add a default option for selection
        return Response.builder()
                .code(200)
                .data(customerResponseList)
                .build();
    }

    public Response getPetsByCustomerId(Long customerId) throws DataNotFoundException {
        List<PaymentCustomersResponse> petResponseList = userService.findByCustomersId(customerId).getPets().stream()
                .map(pet -> {
                    String name = Optional.ofNullable(pet.getName()).orElse("");
                    String type = Optional.ofNullable(pet.getPetType().getName()).orElse("");
                    String typeTh = Optional.ofNullable(pet.getPetType().getNameTh()).orElse("");
                    return PaymentCustomersResponse.builder()
                            .key(pet.getId())
                            .label(String.format("%s - %s", typeTh, name))
                            .build();
                })
                .sorted(Comparator.comparing(PaymentCustomersResponse::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList();
        return Response.builder()
                .code(200)
                .data(petResponseList)
                .build();
    }

    public Response getTagsByTagType(String tagType) {
    // This method is not implemented in the original code, so we will return an empty response.
        // You can implement the logic to fetch tags based on the tagType if needed.
        return Response.builder()
                .code(200)
                .data(new ArrayList<>())
                .build();
    }
}
