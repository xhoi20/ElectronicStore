package com.electronicstore.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCreateDTO {
    private String emri;
    private Long categoryId;
    private BigDecimal cmimi;
    private int sasia;
    private List<Long> supplierIds;
}
