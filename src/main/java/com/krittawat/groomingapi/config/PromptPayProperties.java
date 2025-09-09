package com.krittawat.groomingapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment.promptpay")
public class PromptPayProperties {
    private String receiverPhone;
    private String receiverNationalId;
    private Integer expiresSeconds = 180;

    public String getReceiverPhone() { return receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    public String getReceiverNationalId() { return receiverNationalId; }
    public void setReceiverNationalId(String receiverNationalId) { this.receiverNationalId = receiverNationalId; }
    public Integer getExpiresSeconds() { return expiresSeconds; }
    public void setExpiresSeconds(Integer expiresSeconds) { this.expiresSeconds = expiresSeconds; }
}