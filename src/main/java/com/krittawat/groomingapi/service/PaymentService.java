package com.krittawat.groomingapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krittawat.groomingapi.config.PromptPayProperties;
import com.krittawat.groomingapi.controller.request.CalculateRequest;
import com.krittawat.groomingapi.controller.request.CartItemRequest;
import com.krittawat.groomingapi.controller.request.GenerateQrRequest;
import com.krittawat.groomingapi.controller.response.*;
import com.krittawat.groomingapi.datasource.entity.EInvoiceSession;
import com.krittawat.groomingapi.datasource.entity.EItem;
import com.krittawat.groomingapi.datasource.entity.EPromotion;
import com.krittawat.groomingapi.datasource.entity.EPromotionFreeGiftItem;
import com.krittawat.groomingapi.datasource.service.CalculationSessionService;
import com.krittawat.groomingapi.datasource.service.ItemsService;
import com.krittawat.groomingapi.datasource.service.PromotionService;
import com.krittawat.groomingapi.datasource.service.UserService;
import com.krittawat.groomingapi.error.DataNotFoundException;
import com.krittawat.groomingapi.service.model.CoreCalc;
import com.krittawat.groomingapi.service.model.DiscountResult;
import com.krittawat.groomingapi.service.model.MoreThanPick;
import com.krittawat.groomingapi.utils.EnumUtil;
import com.krittawat.groomingapi.utils.PromptPayUtil;
import com.krittawat.groomingapi.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserService userService;
    private final PromotionService promotionService;
    private final ItemsService itemsService;
    private final CalculationSessionService calculationSessionService;
    private final ObjectMapper objectMapper;
    private final QrService qrService;
    private final PromptPayProperties props;

    private static final int PREVIEW_EXPIRES_SEC = 5;

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
                            .phone(phone)
                            .build();
                })
                .sorted(Comparator.comparing(PaymentCustomersResponse::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList());
        customerResponseList.add(0, PaymentCustomersResponse.builder()
                .key(null)
                .label("ไม่ระบุ")
                .name("No name")
                .phone("")
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
                    .stock(item.getStock())
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

    public Response calculate(CalculateRequest request)
            throws DataNotFoundException, JsonProcessingException {

        final List<CartItemRequest> cartItems = request.getItems();

        // เตรียมข้อมูลต้นทาง
        final List<EPromotion> allPromotions  = promotionService.findByActive();
        final List<FreeGiftResponse> allFreeGifts = promotionService.getAllFreeGifts();

        // คำนวณแกนหลัก (รายการ/ส่วนลดต่อแถว/คำเตือน/โปรฯ MORE_THAN)
        CoreCalc core = computeCore(cartItems, allPromotions, allFreeGifts);

        // เลือกโปรฯ MORE_THAN + ยอดส่วนลดเพิ่มเติม
        MoreThanPick pick = pickMoreThan(core.getTotalAfterItemDiscount(), core.getMoreThanCandidates());

        // รวมยอดสุดท้าย (รวม MORE_THAN เข้าไปด้วย)
        BigDecimal finalTotalDiscount = core.getTotalItemDiscount().add(pick.getMoreThanDiscount());
        BigDecimal finalTotalAfter    = core.getTotalAfterItemDiscount().subtract(pick.getMoreThanDiscount()).max(BigDecimal.ZERO);

        // ทำ cart hash (กันแก้ cart ระหว่าง preview→finalize)
        final String cartHash = sha256Hex(toStableCartJson(cartItems));

        // payload สำหรับตอบกลับ/เก็บ session
        CartCalculationResultResponse result = CartCalculationResultResponse.builder()
                .items(core.getItemResponses())
                .totalBeforeDiscount(core.getTotalBeforeDiscount())
                .totalDiscount(finalTotalDiscount)      // รวม MORE_THAN แล้ว
                .totalAfterDiscount(finalTotalAfter)    // หัก MORE_THAN แล้ว
                .warningPromotions(core.getWarningPromotions())
                .overallPromotion(pick.getOverall())         // อาจเป็น null
                .build();

        final String invoiceNoReq = request.getInvoiceNo();

        final String invoiceNo = (invoiceNoReq != null && !invoiceNoReq.isBlank())
                ? invoiceNoReq
                : LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        EInvoiceSession session = calculationSessionService.getOrNewByInvoiceNo(invoiceNo);
        session.setInvoiceNo(invoiceNo);
        session.setCartHash(cartHash);
        session.setPayloadJson(objectMapper.writeValueAsString(result));
        session.setTotalBefore(core.getTotalBeforeDiscount());
        session.setTotalDiscount(finalTotalDiscount);
        session.setTotalAfter(finalTotalAfter);
        session.setStatus("WAITING PAYMENT");
        calculationSessionService.save(session);

        result.setInvoiceNo(invoiceNo);
        return Response.builder().code(200).data(result).build();

//        // -------- FINALIZE --------
//        if (invoiceNo == null || invoiceNo.isBlank()) {
//            throw new IllegalArgumentException("invoiceNo is required for finalize");
//        }
//
//        ECalculationSession session = calculationSessionService.getByCalculationId(invoiceNo);
//        if (!"PREVIEW".equals(session.getStatus())) {
//            throw new IllegalStateException("Session already finalized or expired");
//        }
//        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
//            session.setStatus("EXPIRED");
//            calculationSessionService.save(session);
//            throw new IllegalStateException("Preview session expired, please recalculate");
//        }
//        if (!Objects.equals(session.getCartHash(), cartHash)) {
//            throw new IllegalStateException("Cart changed since preview, please recalculate");
//        }
//
//        // ตรึงผลตาม preview เด๊ะ ๆ (ป้องกัน drift)
//        CartCalculationResultResponse previewResult =
//                objectMapper.readValue(session.getPayloadJson(), CartCalculationResultResponse.class);
//
//        // หักโควตา (ทางเลือก A: UPDATE อะตอมมิก) ตาม "จำนวนชิ้นของแถมจริง"
//        promotionService.consumePromotionQuotas(previewResult);
//
//        session.setStatus("FINALIZED");
//        session.setFinalizedAt(LocalDateTime.now());
//        calculationSessionService.save(session);
//
//        previewResult.setCalculationId(invoiceNo);
//        previewResult.setExpiresInSec(0);
//        return Response.builder().code(200).data(previewResult).build();
    }

    // คำนวณแกนหลัก: ต่อแถว/ของแถม/คำเตือน/รวมยอด (ยังไม่รวม MORE_THAN)
    private CoreCalc computeCore(List<CartItemRequest> cartItems,
                                 List<EPromotion> allPromotions,
                                 List<FreeGiftResponse> allFreeGifts) throws DataNotFoundException {

        BigDecimal totalBefore = cartItems.stream()
                .map(CartItemRequest::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CartItemResponse> itemResponses = new ArrayList<>();
        BigDecimal totalDiscount = BigDecimal.ZERO;
        List<String> warnings = new ArrayList<>();

        // ของแถมทั้งหมด
        Set<Long> freeGiftItemIds = allFreeGifts.stream()
                .map(FreeGiftResponse::getFreeItemId)
                .collect(Collectors.toSet());

        Map<Long, CartItemRequest> cartById = cartItems.stream()
                .collect(Collectors.toMap(CartItemRequest::getItemId, Function.identity()));

        Map<Long, List<AppliedPromotionResponse>> appliedMap = new HashMap<>();
        Map<Long, BigDecimal> discountMap = new HashMap<>();
        Set<Long> pickedFreeGiftIds = cartItems.stream()
                .map(CartItemRequest::getItemId)
                .filter(freeGiftItemIds::contains)
                .collect(Collectors.toSet());

        List<EPromotion> moreThanPromos = new ArrayList<>();

        // 1) คำนวณไอเท็มปกติ
        for (CartItemRequest item : cartItems) {
            if (pickedFreeGiftIds.contains(item.getItemId())) continue;

            EItem entityItem = itemsService.getById(item.getItemId());
            List<EPromotion> applicable = filterPromotionsForItem(allPromotions, entityItem);

            List<EPromotion> sorted = applicable.stream()
                    .sorted(Comparator.comparing(p -> getDiscountSeq(p.getDiscountType())))
                    .toList();

            BigDecimal itemDiscount = BigDecimal.ZERO;
            List<AppliedPromotionResponse> applied = new ArrayList<>();
            boolean hasNormalApplied = false;

            for (EPromotion promo : sorted) {
                if (promo.getDiscountType() == EnumUtil.DISCOUNT_TYPE.MORE_THAN) {
                    moreThanPromos.add(promo);
                    continue;
                }
                if (hasNormalApplied && promo.getDiscountType() == EnumUtil.DISCOUNT_TYPE.NORMAL) continue;
                if (promo.getDiscountType() == EnumUtil.DISCOUNT_TYPE.NORMAL) hasNormalApplied = true;

                DiscountResult dr = calculateDiscount(item, promo, cartItems);
                if (dr.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    itemDiscount = itemDiscount.add(dr.getAmount());
                    applied.add(toResponse(promo, dr.getAmount()));
                }
                if (promo.getDiscountType() == EnumUtil.DISCOUNT_TYPE.FREE && dr.getGifts() != null && !dr.getGifts().isEmpty()) {
                    warnings.add(String.format("โปรโมชัน '%s' ยังไม่ได้หยิบของแถมมาให้ครบสำหรับสินค้า %s",
                            promo.getName(), item.getName()));
                }
            }
            appliedMap.put(item.getItemId(), applied);
            discountMap.put(item.getItemId(), itemDiscount);
        }

        // 2) คำนวณของแถม (FREE) ไปลงที่ไอเท็มของแถม
        for (CartItemRequest item : cartItems) {
            if (!pickedFreeGiftIds.contains(item.getItemId())) continue;

            List<EPromotion> relatedPromos = allPromotions.stream()
                    .filter(promo -> promotionService.getFreeGiftByPromotionId(promo.getId()).stream()
                            .anyMatch(g -> Objects.equals(g.getFreeItemId(), item.getItemId())))
                    .toList();

            BigDecimal totalItemDiscount = BigDecimal.ZERO;
            List<AppliedPromotionResponse> applied = new ArrayList<>();

            for (EPromotion promo : relatedPromos) {
                List<EPromotionFreeGiftItem> gifts = promotionService.getFreeGiftByPromotionId(promo.getId()).stream()
                        .filter(g -> Objects.equals(g.getFreeItemId(), item.getItemId()))
                        .toList();

                for (EPromotionFreeGiftItem gift : gifts) {
                    CartItemRequest buyItem = cartById.get(gift.getBuyItemId());
                    if (buyItem == null) continue;

                    int requiredQuantity = extractRequiredQuantity(promo.getCondition());
                    int freeQuantity = promo.getAmount().intValue();
                    int eligibleCount = (buyItem.getQuantity() / requiredQuantity) * freeQuantity;

                    int inCartCount = item.getQuantity();
                    int actualFreeCount = Math.min(inCartCount, eligibleCount); // จำนวนของแถมจริง

                    if (actualFreeCount > 0) {
                        BigDecimal discountAmount = item.getPrice().multiply(BigDecimal.valueOf(actualFreeCount));
                        totalItemDiscount = totalItemDiscount.add(discountAmount);

                        applied.add(AppliedPromotionResponse.builder()
                                .promotionId(promo.getId())
                                .name(promo.getName())
                                .discountAmount(discountAmount)
                                .usedUnits(actualFreeCount) // ใช้หักโควตาตอน finalize
                                .build());
                    }
                }
            }
            appliedMap.put(item.getItemId(), applied);
            discountMap.put(item.getItemId(), totalItemDiscount);
        }

        // 3) สร้างรายการตอบกลับต่อแถว + รวมส่วนลดแถว
        for (CartItemRequest item : cartItems) {
            BigDecimal itemDiscount = discountMap.getOrDefault(item.getItemId(), BigDecimal.ZERO);
            List<AppliedPromotionResponse> applied = appliedMap.getOrDefault(item.getItemId(), List.of());

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

        BigDecimal afterItemDiscount = totalBefore.subtract(totalDiscount).max(BigDecimal.ZERO);

        return CoreCalc.builder()
                .itemResponses(itemResponses)
                .totalBeforeDiscount(totalBefore)
                .totalItemDiscount(totalDiscount)
                .totalAfterItemDiscount(afterItemDiscount)
                .warningPromotions(warnings)
                .moreThanCandidates(moreThanPromos)
                .build();
    }

    // เลือกโปรฯ MORE_THAN ที่เข้าเงื่อนไขจากยอดหลังหักส่วนลดแถวแล้ว
    private MoreThanPick pickMoreThan(BigDecimal baseAmount, List<EPromotion> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return MoreThanPick.builder()
                    .overall(null)
                    .moreThanDiscount(BigDecimal.ZERO)
                    .build();
        }

        List<EPromotion> eligible = candidates.stream()
                .map(p -> Map.entry(p, extractConditionAmount(p.getCondition())))
                .filter(e -> e.getValue().compareTo(baseAmount) <= 0)
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        if (eligible.isEmpty()) return MoreThanPick.builder()
                .overall(null)
                .moreThanDiscount(BigDecimal.ZERO)
                .build();

        EPromotion selected = eligible.get(0);
        BigDecimal moreThanDiscount = selected.getAmount();

        AppliedPromotionResponse overall = AppliedPromotionResponse.builder()
                .promotionId(selected.getId())
                .name(selected.getName())
                .discountAmount(moreThanDiscount)
                .build();

        return MoreThanPick.builder()
                .overall(overall)
                .moreThanDiscount(moreThanDiscount)
                .build();
    }


    private String toStableCartJson(List<CartItemRequest> cartItems) {
        List<CartItemRequest> sorted = new ArrayList<>(cartItems);
        sorted.sort(Comparator
                .comparing(CartItemRequest::getKey, Comparator.nullsFirst(String::compareTo))
                .thenComparing(ci -> String.valueOf(ci.getItemId()), Comparator.nullsFirst(String::compareTo)));
        try {
            return objectMapper.writeValueAsString(sorted);
        } catch (Exception e) {
            return String.valueOf(sorted.hashCode());
        }
    }

    private static String sha256Hex(String input) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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


    public Response generateQrCode(GenerateQrRequest req) throws Exception {
        if (props.getReceiverPhone()==null && props.getReceiverNationalId()==null){
            throw new IllegalStateException("PromptPay receiver not configured");
        }
        String calcId = (req.getInvoiceNo() != null && !req.getInvoiceNo().isBlank())
                ? req.getInvoiceNo()
                : UUID.randomUUID().toString();
        BigDecimal amount = req.getAmount(); // validate มาแล้วจาก @Valid
        String payload;
        if (props.getReceiverPhone()!=null){
            payload = PromptPayUtil.buildPayloadPhone(props.getReceiverPhone(), amount);
        } else {
            payload = PromptPayUtil.buildPayloadNationalId(props.getReceiverNationalId(), amount);
        }
        LocalDateTime expiresAt = ZonedDateTime.now().plusSeconds(props.getExpiresSeconds()).toLocalDateTime();
        return Response.builder()
                .code(200)
                .data(GenerateQrResponse.builder()
                    .invoiceNo(calcId)
                    .payload(payload)
                    .imageBase64(qrService.toPngBase64(payload, 240))
                    .contentType("image/png")
                    .expiresAt(expiresAt.toString())
                    .amount(amount.doubleValue())
                    .build())
                .build();
    }
}
