package com.dilip.ccms.crediCard;

import com.dilip.ccms.personalDetails.PersonalDetails;
import com.dilip.ccms.transactions.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditCard {

    @Id
    private Integer id;
    private String cardNumber;
    private String cardHolderName;
    private LocalDate issuedDate;
    private LocalDate expirationDate;
    private Integer cvv;
    private BigDecimal creditLimit;
    private BigDecimal balance;
    private Float interestRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_details_id", referencedColumnName = "id")
    private PersonalDetails personalDetails;

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}
