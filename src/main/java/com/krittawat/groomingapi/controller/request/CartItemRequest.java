package com.krittawat.groomingapi.controller.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemRequest {
    private String key; // Unique identifier for the cart item
    private Long itemId; // ID of the grooming service or pet shop item
    private String name; // Name of the grooming service or pet shop item
    private String type; // Type of item: 'G' for Grooming Service, 'P' for Pet Shop Item
    private Long petId; // ID of the pet for grooming services
    private BigDecimal price; // Price of the grooming service or pet shop item
    private Integer quantity; // Quantity of the item
    private BigDecimal total; // Total price for the item (price * quantity)
}
