package com.electronicstore.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseItemRequest {
    private Long itemId;
    private Long purchaseId;
    private int quantity;

}