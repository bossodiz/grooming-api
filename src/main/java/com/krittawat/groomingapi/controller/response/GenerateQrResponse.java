package com.krittawat.groomingapi.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateQrResponse {
    private String invoiceNo;
    private String payload;          // EMVCo payload (สำหรับ debug/สำรอง)
    private String imageBase64;      // base64 PNG (ไม่รวม prefix)
    private String contentType;      // image/png
    private String expiresAt;        // ISO string
    private Double amount;           // เผื่อ FE ต้องการ echo
}
