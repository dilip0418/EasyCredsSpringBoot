package com.dilip.ccms.crediCard;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreditCardDto {
    private Integer id;
    private String cardNumber;
    private String cardHolderName;
    private LocalDate issuedDate;
    private LocalDate expirationDate;
    private Integer cvv;
    private BigDecimal creditLimit;
    private BigDecimal balance;
    private Float interestRate;
    private Integer cardHolderId;
}
