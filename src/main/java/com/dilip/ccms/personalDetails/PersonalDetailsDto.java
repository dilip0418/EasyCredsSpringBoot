package com.dilip.ccms.personalDetails;

import com.dilip.ccms.address.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDetailsDto {
    @NotBlank(message="First name is required")
    private String firstName;
    @NotBlank(message="First name is required")
    private String lastName;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Annual income should a number")
    private BigDecimal annualIncome;

    @NotNull(message = "Address is required")
    @Valid
    private AddressDTO address;

    @Valid
    @NotNull(message = "EmploymentStatusId is required")
    private Integer employmentStatusId;


    public static PersonalDetailsDto mapToDto(PersonalDetails personalDetails) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(personalDetails.getAddress().getStreet());
        addressDTO.setCityID(personalDetails.getAddress().getCity().getId());
        addressDTO.setStateID(personalDetails.getAddress().getCity().getState().getId());

        PersonalDetailsDto dto = new PersonalDetailsDto();
        dto.setFirstName(personalDetails.getFirstName());
        dto.setLastName(personalDetails.getLastName());
        dto.setDateOfBirth(personalDetails.getDateOfBirth());
        dto.setAnnualIncome(personalDetails.getAnnualIncome());
        dto.setAddress(addressDTO);
        dto.setEmploymentStatusId(personalDetails.getEmploymentStatus().getId());

        return dto;
    }
}
