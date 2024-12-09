package com.dilip.ccms.creditCardApplication;

import com.dilip.ccms.personalDetails.PersonalDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreditCardApplication {
    @Id
    private Integer id;

    private LocalDate ApplicationDate;
    private LocalDate LastModifiedDate;

    @OneToOne
    @JoinColumn(name ="personal_details_id", referencedColumnName = "id")
    private PersonalDetails personalDetails;

    @OneToOne
    @JoinColumn(name ="application_status_id")
    private ApplicationStatus applicationStatus;

    private String email;
    private String phone;

    private BigDecimal annualIncome;
    private String comments;
}
