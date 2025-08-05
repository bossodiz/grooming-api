package com.krittawat.groomingapi.service;

import com.krittawat.groomingapi.controller.request.CartItemRequest;
import com.krittawat.groomingapi.controller.response.*;
import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPromotion;
import com.krittawat.groomingapi.datasource.entity.EPromotionFreeGiftItem;
import com.krittawat.groomingapi.datasource.service.ItemsService;
import com.krittawat.groomingapi.datasource.service.PromotionService;
import com.krittawat.groomingapi.datasource.service.TagService;
import com.krittawat.groomingapi.datasource.service.UserService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.model.DiscountResult;
import com.krittawat.groomingapi.utils.EnumUtil;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserService userService;
    private final TagService tagService;
    private final PromotionService promotionService;
    private final ItemsService itemsService;

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


    public Response getGroomingServices(Long petTypeId) throws DataNotFoundException {
        List<EItem> list = itemsService.getGroomingServiceByPetType(petTypeId).stream()
                .sorted(Comparator.comparing(EItem::getName))
                .toList();
        List<GroomingServiceResponse> response = list.stream()
                .map(service -> GroomingServiceResponse.builder()
                        .id(service.getId())
                        .name(service.getName())
                        .description(service.getRemark())
                        .price(UtilService.toStringDefaulterZero(service.getPrice()))
                        .remark(service.getRemark())
                        .barcode(service.getBarcode())
                        .tags(service.getTags().stream()
                                .map(tag -> ItemTagResponse.builder()
                                        .id(tag.getId())
                                        .name(tag.getName())
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
        List<EItem> list = itemsService.getProducts();
        List<PetShotResponse> responses = list.stream()
                .map(item -> PetShotResponse.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .price(UtilService.toStringDefaulterZero(item.getPrice()))
                    .remark(item.getRemark())
                    .stock(UtilService.toStringDefaulterZero(item.getStock()))
                        .tags(item.getTags().stream()
                                .map(tag -> ItemTagResponse.builder()
                                        .id(tag.getId())
                                        .name(tag.getName())
                                        .build())
                                .toList())
                    .build())
                .toList();
        return Response.builder()
                .code(200)
                .data(responses)
                .build();
    }


    public Response calculate(List<CartItemRequest> cartItems) throws DataNotFoundException {
        List<EPromotion> allPromotions  = promotionService.findByActive();
        List<FreeGiftResponse> allFreeGifts = promotionService.getAllFreeGifts();

        BigDecimal totalBeforeDiscount = cartItems.stream()
                .map(CartItemRequest::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CartItemResponse> itemResponses = new ArrayList<>();
        BigDecimal totalDiscount = BigDecimal.ZERO;
        List<String> warningPromotions = new ArrayList<>();

        // แยก item ที่เป็นของแถมออกจาก item ปกติ
        Set<Long> freeGiftItemIds = allFreeGifts.stream()
                .map(FreeGiftResponse::getFreeItemId)
                .collect(Collectors.toSet());

        // Map เก็บ itemId => CartItemRequest เพื่ออ้างอิงในภายหลัง
        Map<Long, CartItemRequest> cartItemById = cartItems.stream()
                .collect(Collectors.toMap(CartItemRequest::getItemId, Function.identity()));

        // เก็บ applied promotions ของแต่ละ itemId
        Map<Long, List<AppliedPromotionResponse>> appliedPromotionsMap = new HashMap<>();
        // เก็บส่วนลดรวมของแต่ละ itemId
        Map<Long, BigDecimal> discountMap = new HashMap<>();

        // เก็บ itemId ที่เป็นของแถม และถือว่า "หยิบของแถมแล้ว" (ไม่ต้องคิดโปรโมชั่นเพิ่ม)
        Set<Long> freeGiftPickedItemIds = new HashSet<>();

        for (CartItemRequest item : cartItems) {
            if (freeGiftItemIds.contains(item.getItemId())) {
                // ถ้าเป็นของแถม ให้ข้ามการคำนวณโปรโมชั่นอื่น
                freeGiftPickedItemIds.add(item.getItemId());
            }
        }
        List<EPromotion> moreThanPromos = new ArrayList<>();
        for (CartItemRequest item : cartItems) {
            // ข้าม item ที่เป็นของแถม (คำนวณตอนท้ายแยกต่างหาก)
            if (freeGiftPickedItemIds.contains(item.getItemId())) continue;

            EItem entityItem = itemsService.getById(item.getItemId());
            List<EPromotion> applicablePromotions = filterPromotionsForItem(allPromotions, entityItem);

            List<EPromotion> sorted = applicablePromotions.stream()
                    .sorted(Comparator.comparing(p -> getDiscountSeq(p.getDiscountType())))
                    .toList();

            BigDecimal itemDiscount = BigDecimal.ZERO;
            List<AppliedPromotionResponse> applied = new ArrayList<>();
            boolean hasNormalApplied = false;

            for (EPromotion promo : sorted) {
                if (promo.getDiscountType() == EnumUtil.DISCOUNT_TYPE.MORE_THAN) {
                    // เก็บโปรโมชันที่เป็นแบบ "มากกว่า" ไว้คำนวณทีหลัง
                    moreThanPromos.add(promo);
                    continue;
                }
                if (hasNormalApplied && EnumUtil.DISCOUNT_TYPE.NORMAL == promo.getDiscountType()) continue;
                if (EnumUtil.DISCOUNT_TYPE.NORMAL == promo.getDiscountType()) hasNormalApplied = true;

                DiscountResult discount = calculateDiscount(item, promo, cartItems);
                if (discount.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    itemDiscount = itemDiscount.add(discount.getAmount());
                    applied.add(toResponse(promo, discount.getAmount()));
                }
                // ถ้าโปรโมชันเป็นของแถม ให้โยกไปเก็บที่ของแถมแทน (handled later)
                if (promo.getDiscountType() == EnumUtil.DISCOUNT_TYPE.FREE) {
                    // ทำ nothing ที่นี่ เพื่อรวมส่วนลดไปเก็บที่ของแถมตอนหลัง
                    // แต่เก็บ warning ถ้ามีของแถมไม่ครบ
                    if (discount.getGifts() != null && !discount.getGifts().isEmpty()) {
                        String warning = String.format(
                                "โปรโมชัน '%s' ยังไม่ได้หยิบของแถมมาให้ครบสำหรับสินค้า %s",
                                promo.getName(), item.getName()
                        );
                        warningPromotions.add(warning);
                    }
                }
            }

            appliedPromotionsMap.put(item.getItemId(), applied);
            discountMap.put(item.getItemId(), itemDiscount);
        }

        // คำนวณโปรโมชั่นของแถม (FREE) และโยก applied promotion ไปให้ของแถม
        for (CartItemRequest item : cartItems) {
            if (!freeGiftPickedItemIds.contains(item.getItemId())) continue;

            // หาของแถมที่เกี่ยวข้อง
            List<FreeGiftResponse> giftsForItem = allFreeGifts.stream()
                    .filter(g -> Objects.equals(g.getFreeItemId(), item.getItemId()))
                    .toList();

            if (giftsForItem.isEmpty()) {
                // ไม่มีของแถมที่เกี่ยวข้องจริง ๆ
                appliedPromotionsMap.put(item.getItemId(), List.of());
                discountMap.put(item.getItemId(), BigDecimal.ZERO);
                continue;
            }

            // หา promotion ที่เกี่ยวข้องกับของแถมนี้
            List<EPromotion> relatedPromos = allPromotions.stream()
                    .filter(promo -> promotionService.getFreeGiftByPromotionId(promo.getId()).stream()
                            .anyMatch(g -> Objects.equals(g.getFreeItemId(), item.getItemId())))
                    .toList();

            BigDecimal totalItemDiscount = BigDecimal.ZERO;
            List<AppliedPromotionResponse> applied = new ArrayList<>();

            for (EPromotion promo : relatedPromos) {
                // หาจำนวนที่ลูกค้าควรจะได้รับของแถมนี้
                List<EPromotionFreeGiftItem> gifts = promotionService.getFreeGiftByPromotionId(promo.getId()).stream()
                        .filter(g -> Objects.equals(g.getFreeItemId(), item.getItemId()))
                        .toList();

                for (EPromotionFreeGiftItem gift : gifts) {
                    CartItemRequest buyItem = cartItemById.get(gift.getBuyItemId());
                    if (buyItem == null) continue;

                    int requiredQuantity = extractRequiredQuantity(promo.getCondition()); // → จะได้ 1
                    int freeQuantity = promo.getAmount().intValue();
                    int eligibleCount = (buyItem.getQuantity() / requiredQuantity) * freeQuantity;

                    // จำนวนที่ลูกค้าหยิบของแถมนี้เข้าตะกร้า
                    int inCartCount = item.getQuantity();
                    int actualFreeCount = Math.min(inCartCount, eligibleCount); // ✅ สำคัญที่สุด

                    if (actualFreeCount > 0) {
                        BigDecimal discountAmount = item.getPrice().multiply(BigDecimal.valueOf(actualFreeCount));
                        totalItemDiscount = totalItemDiscount.add(discountAmount);

                        applied.add(AppliedPromotionResponse.builder()
                                .promotionId(promo.getId())
                                .name(promo.getName())
                                .discountAmount(discountAmount)
                                .build());
                    }
                }
            }

            appliedPromotionsMap.put(item.getItemId(), applied);
            discountMap.put(item.getItemId(), totalItemDiscount);
        }

        // สร้าง response รายการสินค้า
        for (CartItemRequest item : cartItems) {
            BigDecimal itemDiscount = discountMap.getOrDefault(item.getItemId(), BigDecimal.ZERO);
            List<AppliedPromotionResponse> applied = appliedPromotionsMap.getOrDefault(item.getItemId(), List.of());

            BigDecimal finalTotal = item.getTotal().subtract(itemDiscount);
            if (finalTotal.compareTo(BigDecimal.ZERO) < 0) finalTotal = BigDecimal.ZERO;

            itemResponses.add(CartItemResponse.builder()
                    .key(item.getKey())
                    .itemId(item.getItemId())
                    .name(item.getName())
                    .price(item.getPrice())
                    .quantity(item.getQuantity())
                    .total(item.getTotal())
                    .discount(itemDiscount)
                    .finalTotal(finalTotal)
                    .appliedPromotions(applied)
                    .build());

            totalDiscount = totalDiscount.add(itemDiscount);
        }

        BigDecimal totalAfterDiscount = totalBeforeDiscount.subtract(totalDiscount);
        if (totalAfterDiscount.compareTo(BigDecimal.ZERO) < 0) {
            totalAfterDiscount = BigDecimal.ZERO;
        }

        final BigDecimal finalTotalAfterDiscount = totalAfterDiscount; // ✅ ทำให้ใช้ใน lambda ได้

        BigDecimal moreThanDiscount = BigDecimal.ZERO;
        EPromotion selectedMoreThanPromo = null;

        List<EPromotion> eligibleMoreThanPromos = moreThanPromos.stream()
                .map(p -> {
                    BigDecimal conditionAmount = extractConditionAmount(p.getCondition());
                    return new AbstractMap.SimpleEntry<>(p, conditionAmount);
                })
                .filter(e -> e.getValue().compareTo(finalTotalAfterDiscount) <= 0) // ✅ ใช้ตัวนี้แทน
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        if (!eligibleMoreThanPromos.isEmpty()) {
            selectedMoreThanPromo = eligibleMoreThanPromos.get(0);
            moreThanDiscount = selectedMoreThanPromo.getAmount();
        }

        AppliedPromotionResponse overall = null;
        if (selectedMoreThanPromo != null) {
            overall = AppliedPromotionResponse.builder()
                    .promotionId(selectedMoreThanPromo.getId())
                    .name(selectedMoreThanPromo.getName())
                    .discountAmount(moreThanDiscount)
                    .build();
        }

        return Response.builder()
                .code(200)
                .data(CartCalculationResultResponse.builder()
                    .items(itemResponses)
                    .totalBeforeDiscount(totalBeforeDiscount)
                    .totalDiscount(totalDiscount.add(moreThanDiscount))
                    .totalAfterDiscount(totalAfterDiscount.subtract(moreThanDiscount))
                    .warningPromotions(warningPromotions)
                    .overallPromotion(overall)
                    .build())
                .build();
    }

    private List<EPromotion> filterPromotionsForItem(List<EPromotion> allPromotions, EItem item) {
        return allPromotions.stream()
                .filter(p -> EnumUtil.PROMOTION_TYPE.ALL == p.getDiscountCategory() || p.getDiscountCategory().name().equalsIgnoreCase(item.getItemType().name()))
                .filter(p -> {
                    boolean includedEmpty = itemsService.countIncludedByPromotionId(p.getId()) == 0;
                    boolean excludedEmpty = itemsService.countExcludedByPromotionId(p.getId()) == 0;
                    boolean isIncluded = includedEmpty || itemsService.existsIncludedByPromotionIdAndItemId(p.getId(), item.getId());
                    boolean isExcluded = !excludedEmpty && itemsService.existsExcludedByPromotionIdAndItemId(p.getId(), item.getId());
                    return isIncluded && !isExcluded;
                }).toList();
    }

    private int getDiscountSeq(EnumUtil.DISCOUNT_TYPE discountType) {
        return switch (discountType) {
            case FREE -> 0;
            case NORMAL -> 1;
            case MORE_THAN -> 2;
            default -> 99;
        };
    }

    private AppliedPromotionResponse toResponse(EPromotion p, BigDecimal amount) {
        return AppliedPromotionResponse.builder()
                .promotionId(p.getId())
                .name(p.getName())
                .discountAmount(amount)
                .build();
    }

    private DiscountResult calculateDiscount(CartItemRequest item, EPromotion promo, List<CartItemRequest> allCartItems) throws DataNotFoundException {
        if (promo.getQuota() != null && promo.getQuota() != -1 && promo.getQuota() <= 0) {
            return DiscountResult.noDiscount();
        }
        BigDecimal total = item.getTotal();
        switch (promo.getDiscountType()) {
            case NORMAL:
                return calculateNormalDiscount(total, promo);
            case FREE:
                return calculateFreeGiftDiscount(item, promo, allCartItems);
        }
        return DiscountResult.noDiscount();
    }


    private DiscountResult calculateNormalDiscount(BigDecimal total, EPromotion promo) {
        if (EnumUtil.AMOUNT_TYPE.PERCENT == promo.getAmountType()) {
            BigDecimal discount = total.multiply(promo.getAmount()).divide(BigDecimal.valueOf(100));
            return DiscountResult.of(discount);
        } else if (EnumUtil.AMOUNT_TYPE.BAHT == promo.getAmountType()) {
            return DiscountResult.of(promo.getAmount());
        }
        return DiscountResult.noDiscount();
    }

    private DiscountResult calculateFreeGiftDiscount(
            CartItemRequest item,
            EPromotion promo,
            List<CartItemRequest> allCartItems
    ) {
        // ดึงของแถมที่ผูกกับ item นี้จาก promotion เดียวกัน
        List<FreeGiftResponse> giftsForItem = promotionService.getFreeGiftByPromotionId(promo.getId()).stream()
                .filter(g -> Objects.equals(g.getBuyItemId(), item.getItemId()))
                .map(g -> {
                    // ดึงค่า requiredQuantity จาก condition เช่น "BUY_1"
                    int requiredQuantity = extractRequiredQuantity(promo.getCondition()); // → จะได้ 1

                    // ของแถมที่ได้ทั้งหมดต่อการซื้อครบตามจำนวน
                    int freeQuantity = promo.getAmount().intValue();

                    // จำนวนของแถมที่ลูกค้ามีสิทธิ์ได้รับ
                    int eligibleGiftCount = (item.getQuantity() / requiredQuantity) * freeQuantity;

                    // ตรวจสอบว่าลูกค้าหยิบของแถมชิ้นนี้เข้าตะกร้ามาแล้วกี่ชิ้น
                    int inCartCount = allCartItems.stream()
                            .filter(ci -> Objects.equals(ci.getItemId(), g.getFreeItemId()))
                            .mapToInt(CartItemRequest::getQuantity)
                            .sum();
                    EItem itemFree = null;
                    try {
                        itemFree = itemsService.getById(g.getFreeItemId());
                    } catch (DataNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return FreeGiftResponse.builder()
                            .buyItemId(g.getBuyItemId())
                            .buyItemName(item.getName())
                            .freeItemId(g.getFreeItemId())
                            .freeItemName(itemFree.getName())
                            .requiredQuantity(requiredQuantity)
                            .freeQuantity(freeQuantity)
                            .alreadyInCart(inCartCount >= eligibleGiftCount)
                            .inCartQuantity(inCartCount)
                            .build();
                })
                .toList();


        if (giftsForItem.isEmpty()) {
            return new DiscountResult(BigDecimal.ZERO, List.of());
        }

        // จำนวนของแถมที่ได้ต่อชิ้นที่ซื้อ (เช่น ซื้อ 1 แถม 2)
        int quantityPerGift = promo.getAmount().intValue();
        int eligibleGiftCount = item.getQuantity() * quantityPerGift;

        // itemId ของของแถมที่เกี่ยวข้อง
        List<Long> eligibleFreeItemIds = giftsForItem.stream()
                .map(FreeGiftResponse::getFreeItemId)
                .toList();

        // ตรวจสอบว่าผู้ใช้หยิบของแถมเองมาแล้วกี่ชิ้น
        int userGiftCount = allCartItems.stream()
                .filter(ci -> eligibleFreeItemIds.contains(ci.getItemId()))
                .mapToInt(CartItemRequest::getQuantity)
                .sum();

        // คำนวณของแถมที่ "ควรได้" แต่ยัง "ไม่ได้หยิบมา"
        int missingGiftCount = Math.max(eligibleGiftCount - userGiftCount, 0);

        // สร้างรายการของแถมที่ยังไม่ได้หยิบ
        List<FreeGiftResponse> missingFreeGifts = new ArrayList<>();
        for (int i = 0; i < missingGiftCount; i++) {
            // วนซ้ำใช้ของแถมในลิสต์เดิม ถ้าไม่พอ
            FreeGiftResponse gift = giftsForItem.get(i % giftsForItem.size());
            missingFreeGifts.add(gift);
        }
        return new DiscountResult(BigDecimal.ZERO, missingFreeGifts);
    }

    private BigDecimal extractThreshold(String condition) {
        if (condition == null) return BigDecimal.ZERO;
        Pattern pattern = Pattern.compile("AMOUNT>(\\d+(\\.\\d+)?)");
        Matcher matcher = pattern.matcher(condition);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }
        return BigDecimal.ZERO;
    }

    private int extractRequiredQuantity(String condition) {
        if (condition != null && condition.startsWith("BUY_")) {
            try {
                return Integer.parseInt(condition.substring(4));
            } catch (NumberFormatException e) {
                // Default fallback ถ้า parsing ไม่ได้
                return 1;
            }
        }
        return 1; // Default
    }

    private BigDecimal extractConditionAmount(String condition) {
        if (condition != null && condition.startsWith("AMOUNT>")) {
            try {
                String amountStr = condition.substring("AMOUNT>".length());
                return new BigDecimal(amountStr);
            } catch (NumberFormatException e) {
                // กรณี format ผิด
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }
}
