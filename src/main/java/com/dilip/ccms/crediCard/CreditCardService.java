package com.dilip.ccms.crediCard;

import com.dilip.ccms.creditCardApplication.CreditCardApplicationDto;
import com.dilip.ccms.personalDetails.PersonalDetailsRepository;
import com.dilip.ccms.transactions.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final PersonalDetailsRepository personalDetailsRepository;

    @Async
    public void createCreditCardAsync(CreditCardApplicationDto application) {

        var cardHolder = personalDetailsRepository.findById(application.getApplicantId()).orElseThrow();
        var creditLimitAndInterestRate = CreditCardBO.getCreditLimitAndInterestRate(
                cardHolder.getEmploymentStatus().getId(),
                application.getAnnualIncome());

        var creditCard = CreditCard.builder()
                .issuedDate(LocalDate.now())
                .expirationDate(LocalDate.now().plusYears(5))
                .transactions(new ArrayList<Transaction>())
                .personalDetails(cardHolder)
                .cardNumber(CreditCardBO.generateCardNumber(16))
                .cvv(CreditCardBO.generateCVV(4))
                .interestRate(creditLimitAndInterestRate.getInterestRate())
                .creditLimit(creditLimitAndInterestRate.getCreditLimit())
                .balance(BigDecimal.ZERO)
                .cardHolderName(cardHolder.getFirstName() + " " + cardHolder.getLastName())
                .build();

        creditCardRepository.save(creditCard);
    }

    public CreditCard getCreditCard(Integer id) {
        return creditCardRepository.findById(id).orElseThrow();
    }

    public List<CreditCard> getCreditCards() {
        var cards = creditCardRepository.findAll();
        if (cards.isEmpty()) {
            throw new RuntimeException("No Credit cards found");
        } else {
            return cards;
        }
    }

    public CreditCard updateCreditCard(CreditCardDto dto) {
        var card = this.getCreditCard(dto.getId());
        if (card != null) {
            card.setCreditLimit(dto.getCreditLimit());
            card.setBalance(dto.getBalance());
            card.setExpirationDate(dto.getExpirationDate());
            card.setCardNumber(dto.getCardNumber());
            card.setCvv(dto.getCvv());
            card.setInterestRate(dto.getInterestRate());

            return creditCardRepository.save(card);
        } else {
            throw new RuntimeException("Card not found");
        }
    }

    public void deleteCard(Integer id) {
        var card = this.getCreditCard(id);
        if (card != null) {
            creditCardRepository.deleteById(id);
        }
    }
}
