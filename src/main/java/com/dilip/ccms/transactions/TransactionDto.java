package com.dilip.ccms.transactions;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Integer id;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private Integer creditCardId;
    private Integer categoryId;
    private Integer typeId;
}
