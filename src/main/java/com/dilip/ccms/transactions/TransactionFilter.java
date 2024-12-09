package com.dilip.ccms.transactions;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionFilter {
    public BigDecimal startAmount;
    public BigDecimal endAmount;
    public LocalDate startDate;
    public LocalDate endDate;
    public Integer categoryId;
    public Integer typeId;
}
