package com.electronicstore.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemUpdateDTO {
    private String emri;
    private BigDecimal cmimi;
    private Integer sasia;
}
