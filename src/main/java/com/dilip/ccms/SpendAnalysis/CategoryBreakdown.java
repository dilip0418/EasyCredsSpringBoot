package com.dilip.ccms.SpendAnalysis;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryBreakdown {
    private Integer categoryId;
    private BigDecimal totalSpend;
    private Double percentage;
}