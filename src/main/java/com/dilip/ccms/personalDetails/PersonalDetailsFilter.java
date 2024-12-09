package com.dilip.ccms.personalDetails;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PersonalDetailsFilter {
    private String firstName;
    private String lastName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer cityId;
    private Integer stateId;
    private Integer employmentStatusId;
    private BigDecimal minAnnualIncome;
    private BigDecimal maxAnnualIncome;
}
