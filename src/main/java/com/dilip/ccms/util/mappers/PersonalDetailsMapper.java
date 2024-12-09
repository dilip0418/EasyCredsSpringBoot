package com.dilip.ccms.util.mappers;

import com.dilip.ccms.address.Address;
import com.dilip.ccms.personalDetails.EmploymentStatus;
import com.dilip.ccms.personalDetails.PersonalDetails;
import com.dilip.ccms.personalDetails.PersonalDetailsDto;
import com.dilip.ccms.user.User;

public class PersonalDetailsMapper {
    public static PersonalDetails toEntity(PersonalDetailsDto dto, Address address, User user, EmploymentStatus status) {
        return PersonalDetails.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dateOfBirth(dto.getDateOfBirth())
                .address(address)
                .employmentStatus(status)
                .build();
    }
    public static PersonalDetailsDto toDto(PersonalDetails personalDetails){
        return PersonalDetailsDto.builder()
                .firstName(personalDetails.getFirstName())
                .lastName(personalDetails.getLastName())
                .dateOfBirth(personalDetails.getDateOfBirth())
                .address(AddressMapper.toDto(personalDetails.getAddress()))
                .employmentStatusId(personalDetails.getEmploymentStatus().getId())
                .build();
    }
}
