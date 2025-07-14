package com.electronicstore.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PurchaseUpdateDTO {
    private Long userId;
    @Nullable
    private LocalDateTime dataBlerjes;
    @Nullable
    private Long furnitorId;
    @Nullable
    private Double totaliKostos;
    @Nullable
    private Integer sasia;
}
