package com.dilip.ccms.SpendAnalysis;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySpend {
    private Integer month;
    private BigDecimal totalSpend;
}
