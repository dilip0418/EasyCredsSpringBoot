package com.dilip.ccms.SpendAnalysis;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpendAnalysis {
    @Id
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private BigDecimal totalSpend;
    private Integer month;
    private Integer year;
}
