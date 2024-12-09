package com.dilip.ccms.creditCardApplication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreditCardApplicationDto {
    private Integer id;
    private LocalDate applicationDate;
    private LocalDate lastModifiedDate;
    @NotNull(message = "Application Status Cannot be null")
    private Integer applicationStatusId;
    @NotNull(message = "Applicant Cannot be null")
    private Integer applicantId; // PersonalDetailsId
    @NotNull(message = "Email Cannot be null")
    @Email()
    private String email;
    @NotNull(message = "PhoneNo Cannot be null")
    private String phone;
    @NotNull(message = "Annual Income Cannot be null")
    private BigDecimal annualIncome;
    private String comments;

    public static CreditCardApplicationDto toDto(CreditCardApplication application){
        return CreditCardApplicationDto.builder()
                .id(application.getId())
                .applicationDate(application.getApplicationDate())
                .lastModifiedDate(application.getLastModifiedDate())
                .applicantId(application.getPersonalDetails().getId())
                .phone(application.getPhone())
                .email(application.getEmail())
                .annualIncome(application.getAnnualIncome())
                .comments(application.getComments())
                .build();
    }
}
