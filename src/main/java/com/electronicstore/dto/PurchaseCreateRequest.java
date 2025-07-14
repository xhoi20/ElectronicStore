package com.electronicstore.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseCreateRequest {
    private Long userId;
    private LocalDateTime dataBlerjes;
    private Long furnitorId;
    private double totaliKostos;
    private int sasia;

}
