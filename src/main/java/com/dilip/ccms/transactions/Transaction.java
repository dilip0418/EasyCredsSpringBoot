package com.dilip.ccms.transactions;

import com.dilip.ccms.crediCard.CreditCard;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    private Integer id;
    private LocalDate transactionDate;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id")
    private CreditCard creditCard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private TransactionCategory category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private TransactionType type;
}
