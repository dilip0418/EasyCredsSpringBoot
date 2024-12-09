package com.dilip.ccms.crediCard;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class CreditCardBO {
    // Generate a random valid credit card number with an optional prefix
    public static String generateCardNumber(int length) {
        // Default prefix to an empty string if not provided
        StringBuilder cardNumber = new StringBuilder();

        // Validate length
        if (length <= cardNumber.length()) {
            throw new IllegalArgumentException("Length must be greater than the prefix length.");
        }

        // Generate random digits to fill the card number to desired length - 1
        Random random = new Random();
        while (cardNumber.length() < length - 1) {
            cardNumber.append(random.nextInt(10));
        }

        // Calculate and append the check digit
        int checkDigit = calculateLuhnCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit);

        return cardNumber.toString();
    }

    // Calculate Luhn Checksum for a given number (excluding check digit)
    public static int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;

        // Process digits from right to left
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        // Calculate the check digit
        return (10 - (sum % 10)) % 10;
    }

    public static Integer generateCVV(int length) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append(r.nextInt(1000));
        }
        return Integer.parseInt(sb.toString());
    }


    public static CreditLimit getCreditLimitAndInterestRate(Integer employmentStatus, BigDecimal annualIncome) {
        var creditLimit = new CreditLimit();

// Determine credit limit based on income bands and employment status
        if (annualIncome.compareTo(new BigDecimal(100000)) >= 0 && (employmentStatus == 3 || employmentStatus == 1)) {
            creditLimit.setCreditLimit(new BigDecimal(50000));
            creditLimit.setInterestRate(0.15f); // 15% interest rate for high-income, stable employment
        } else if (annualIncome.compareTo(new BigDecimal(50000)) >= 0 && employmentStatus == 3) {
            creditLimit.setCreditLimit(new BigDecimal(20000));
            creditLimit.setInterestRate(0.18f); // 18% interest rate for medium income, full-time employment
        } else if (annualIncome.compareTo(new BigDecimal(50000)) >= 0 && employmentStatus == 4) {
            creditLimit.setCreditLimit(new BigDecimal(15000));
            creditLimit.setInterestRate(0.20f); // 20% interest rate for medium income, part-time employment
        } else if (annualIncome.compareTo(new BigDecimal(30000)) >= 0 && employmentStatus == 3) {
            creditLimit.setCreditLimit(new BigDecimal(10000));
            creditLimit.setInterestRate(0.22f); // 22% interest rate for lower income, full-time employment
        } else {
            creditLimit.setCreditLimit(new BigDecimal(5000));
            creditLimit.setInterestRate(0.25f); // 25% interest rate for low income or unstable employment
        }
        return creditLimit;
    }
}
