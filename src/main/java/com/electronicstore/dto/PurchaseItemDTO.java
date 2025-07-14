package com.electronicstore.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PurchaseItemDTO {

    private Long userId;


    private Long purchaseId;

    private Long itemId;
    private Integer quantity;


    private Long invoiceId;
}
