package com.dilip.ccms.personalDetails;

import com.dilip.ccms.address.Address;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class PersonalDetails {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToOne()
    @JoinColumn(name = "employment_status_id", referencedColumnName = "id")
    private EmploymentStatus employmentStatus;

    private BigDecimal annualIncome;
}