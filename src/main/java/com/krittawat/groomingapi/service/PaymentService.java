package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.CalculatePaymentRequest;
import com.krittawat.groomingapi.controller.response.*;
import com.krittawat.groomingapi.datasource.entity.EGroomingService;
import com.krittawat.groomingapi.datasource.entity.EProduct;
import com.krittawat.groomingapi.datasource.entity.ETagItem;
import com.krittawat.groomingapi.datasource.service.GroomingServiceService;
import com.krittawat.groomingapi.datasource.service.ProductService;
import com.krittawat.groomingapi.datasource.service.TagService;
import com.krittawat.groomingapi.datasource.service.UserService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.utils.EnumUtil;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserService userService;
    private final GroomingServiceService groomingServiceService;
    private final ProductService productService;
    private final TagService tagService;

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
                    return PaymentCustomersResponse.builder()
                            .key(e.getId())
                            .label(label)
                            .name(name)
                            .build();
                })
                .sorted(Comparator.comparing(PaymentCustomersResponse::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList());
        customerResponseList.add(0, PaymentCustomersResponse.builder()
                .key(null)
                .label("ไม่ระบุ")
                .name("No name")
                .build()); // Add a default option for selection
        return Response.builder()
                .code(200)
                .data(customerResponseList)
                .build();
    }

    public Response getPetsByCustomerId(Long customerId) throws DataNotFoundException {
        List<PaymentPetResponse> petResponseList = userService.findByCustomersId(customerId).getPets().stream()
                .map(pet -> {
                    String name = Optional.ofNullable(pet.getName()).orElse("");
                    String typeTh = Optional.ofNullable(pet.getPetType().getNameTh()).orElse("");
                    return PaymentPetResponse.builder()
                            .key(pet.getId())
                            .label(String.format("%s - %s", typeTh, name))
                            .name(name)
                            .petTypeId(pet.getTypeId())
                            .build();
                })
                .sorted(Comparator.comparing(PaymentPetResponse::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList();
        return Response.builder()
                .code(200)
                .data(petResponseList)
                .build();
    }


    public Response getGroomingServices(Long petTypeId) {
        List<EGroomingService> list = groomingServiceService.getGroomingServicesByPetType(petTypeId).stream()
                .sorted(Comparator.comparing((EGroomingService e) -> e.getPetType().getId())
                        .thenComparing(EGroomingService::getSequence))
                .toList();
        List<ETagItem> tagItems = tagService.getTagItemsByTagType(EnumUtil.TAG_TYPE.GROOMING);
        List<GroomingServiceResponse> response = list.stream()
                .map(service -> GroomingServiceResponse.builder()
                        .id(service.getId())
                        .nameTh(service.getNameTh())
                        .nameEn(service.getNameEn())
                        .description(service.getRemark())
                        .typeId(service.getPetType().getId())
                        .price(UtilService.toStringDefaulterZero(service.getPrice()))
                        .tags(tagItems.stream().filter(
                                tagItem -> Objects.equals(tagItem.getItemId(), service.getId()))
                                .map(tag -> ItemTagResponse.builder()
                                        .id(tag.getTag().getId())
                                        .name(tag.getTag().getName())
                                        .build())
                                .toList())
                        .build())
                .toList();
        return Response.builder()
                .code(200)
                .data(response)
                .build();
    }

    public Response getPetShopProduct() {
        List<EProduct> list = productService.getProducts();
        List<ETagItem> tagItems = tagService.getTagItemsByTagType(EnumUtil.TAG_TYPE.PET_SHOP);
        List<PetShotResponse> responses = list.stream()
                .map(item -> PetShotResponse.builder()
                    .id(item.getId())
                    .nameTh(item.getNameTh())
                    .nameEn(item.getNameEn())
                    .price(UtilService.toStringDefaulterZero(item.getPrice()))
                    .description(item.getRemark())
                    .stock(UtilService.toStringDefaulterZero(item.getStock()))
                    .tags(tagItems.stream().filter(
                            tagItem -> Objects.equals(tagItem.getItemId(), item.getId()))
                                .map(tag -> ItemTagResponse.builder()
                                        .id(tag.getTag().getId())
                                        .name(tag.getTag().getName())
                                        .build())
                                .toList())
                    .build())
                .toList();
        return Response.builder()
                .code(200)
                .data(responses)
                .build();
    }

    public Response calculate(CalculatePaymentRequest request) {
        return null;
    }
}
