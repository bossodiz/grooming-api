package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeGiftResponse {
    private Long buyItemId;        // ID ของสินค้าที่ต้องซื้อ
    private String buyItemName;    // ชื่อสินค้าที่ต้องซื้อ
    private Long freeItemId;       // ID ของสินค้าที่แถม
    private String freeItemName;   // ชื่อสินค้าที่แถม
    private int requiredQuantity;  // ต้องซื้อกี่ชิ้นถึงจะได้แถม
    private int freeQuantity;      // ได้แถมกี่ชิ้น
    private boolean alreadyInCart; // true ถ้าลูกค้าหยิบของแถมมาใส่แล้ว
    private int inCartQuantity;    // จำนวนของแถมที่หยิบมาใส่ในตะกร้าแล้ว
}
