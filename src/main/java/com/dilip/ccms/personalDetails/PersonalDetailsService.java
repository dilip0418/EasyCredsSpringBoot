package com.dilip.ccms.personalDetails;

import com.dilip.ccms.exception.ResourceNotFoundException;
import com.dilip.ccms.address.*;
import com.dilip.ccms.user.User;
import com.dilip.ccms.user.UserRepository;
import com.dilip.ccms.util.PageResponse;
import com.dilip.ccms.util.mappers.AddressMapper;
import com.dilip.ccms.util.mappers.PersonalDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalDetailsService {

    private final PersonalDetailsRepository personalDetailsRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    private final Logger logger = LoggerFactory.getLogger(PersonalDetailsService.class);
    private final UserRepository userRepository;
    private final EmploymentStatusRepository employmentStatusRepository;

    public PersonalDetailsInfo getPersonalDetailsById(Integer id) {
        logger.info("Finding personal details with id {}", id);
        PersonalDetailsInfo personalDetails = personalDetailsRepository.findProjectedById(id)
                .orElseThrow(() -> new RuntimeException("Could not find personal details with id " + id));

        logger.info("Personal details with id {} retrieved successfully", id);
        return personalDetails;
    }

    public PersonalDetailsDto createPersonalDetails(PersonalDetailsDto personalDetailsDto, Authentication connectedUser) {

        // check if the user already having a personal details record.
        // build User entity
        String userName = connectedUser.getName();
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new RuntimeException("User not found"));


        if (user.getPersonalDetails() != null) {
            throw new RuntimeException("User already has a personal details record");
        }

        // Check if state, city & employmentStatus with id exists in the database
        logger.info("Checking if state & city with id exists in the database");
        State state = stateRepository.findById(personalDetailsDto.getAddress().getStateID())
                .orElseThrow(() -> new RuntimeException("State not found with ID" + personalDetailsDto.getAddress().getStateID()));
        City city = cityRepository.findById(personalDetailsDto.getAddress().getCityID())
                .orElseThrow(() -> new RuntimeException("City not found with ID" + personalDetailsDto.getAddress().getCityID()));
        EmploymentStatus status = employmentStatusRepository.findById(personalDetailsDto.getEmploymentStatusId())
                .orElseThrow(() -> new RuntimeException("status not found with ID" + personalDetailsDto.getEmploymentStatusId()));

        logger.info("City and state found!");

        // build Address entity
        Address address = AddressMapper.toEntity(personalDetailsDto.getAddress(), state, city);

        // build PersonDetails entity
        PersonalDetails personalDetails = PersonalDetailsMapper.toEntity(personalDetailsDto, address, user, status);

        // save to database
        PersonalDetails savedPersonalDetails = personalDetailsRepository.save(personalDetails);
        logger.info("Saved personal details to database with id: {}", savedPersonalDetails.getId());

        user.setPersonalDetails(savedPersonalDetails);
        userRepository.save(user);

        return PersonalDetailsMapper.toDto(savedPersonalDetails);
    }

    private boolean userAlreadyHasPersonalDetails(User user) {
        var personalDetails = userRepository.findByEmail(user.getEmail());
        return personalDetails.isPresent();
    }


    public PageResponse<PersonalDetailsInfo> getAllPersonalDetails(int page, int size, int status) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

        Page<PersonalDetailsInfo> details;
        if (status == 0)
            details = personalDetailsRepository.findProjectedBy(pageable);
        else
            details = personalDetailsRepository.findProjectedByEmploymentStatus_Id(status, pageable);
        List<PersonalDetailsInfo> personalDetails = details.stream().toList();
        return new PageResponse<>(
                personalDetails,
                details.getNumber(),
                details.getSize(),
                details.getTotalElements(),
                details.getTotalPages(),
                details.isFirst(),
                details.isLast()
        );
    }


    public PersonalDetailsInfo updatePersonalDetails(Integer id, PersonalDetailsDto request) throws ResourceNotFoundException {
        var personalDetails = personalDetailsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PersonalDetails not found for ID: " + id));
        personalDetails.setFirstName(request.getFirstName());
        personalDetails.setLastName(request.getLastName());
        personalDetails.setDateOfBirth(request.getDateOfBirth());

        AddressDTO addressDto = request.getAddress();
        if (addressDto != null) {
            Address address = personalDetails.getAddress();
            if (address == null) {
                address = new Address();
            }

            // Update the address fields
            address.setStreet(addressDto.getStreet());

            // Fetch the city, state & employmentStatus from the database
            City city = cityRepository.findById(addressDto.getCityID())
                    .orElseThrow(() -> new ResourceNotFoundException("City not found for ID: " + addressDto.getCityID()));

            State state = stateRepository.findById(addressDto.getStateID())
                    .orElseThrow(() -> new ResourceNotFoundException("State not found for ID: " + addressDto.getStateID()));

            EmploymentStatus status = employmentStatusRepository.findById(request.getEmploymentStatusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found for ID: " + request.getEmploymentStatusId()));

            // Update the state fields in city also.
            city.setState(state);

            address.setCity(city);
            address.setState(state);

            // Set the updated address back to personal details
            personalDetails.setAddress(address);
            personalDetails.setEmploymentStatus(status);
        }
        // Save and return the updated PersonalDetails
        personalDetailsRepository.save(personalDetails);
        return personalDetailsRepository.findProjectedById(personalDetails.getId()).get();
    }

    public boolean deletePersonalDetails(Integer Id) throws ResourceNotFoundException {
        try {
            var user = userRepository.findByPersonalDetailsId(Id);
            if (user != null) {
                user.setPersonalDetails(null);
                userRepository.save(user);
                var personalDetails = personalDetailsRepository.findById(Id);
                if (personalDetails.isPresent()) {
                    personalDetails.ifPresent(personalDetailsRepository::delete);
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        return false;
    }

//    public PageResponse<PersonalDetailsDto> getFilteredDetails(PersonalDetailsFilter filter, int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
//        Specification<PersonalDetails> specification = PersonalDetailsSpecifications.byFilter(filter);
//        System.out.println("Generated Specification: " + specification);
//        Page<PersonalDetails> personalDetailsPage = personalDetailsRepository.findAll(specification, pageable);
//
//        var result = personalDetailsPage.map(PersonalDetailsDto::mapToDto);
//
//        return new PageResponse<PersonalDetailsDto>(
//                result.getContent(),
//                result.getNumber(),
//                result.getSize(),
//                result.getTotalElements(),
//                result.getTotalPages(),
//                result.isFirst(),
//                result.isLast()
//        );
//    }

    public PageResponse<PersonalDetailsDto> searchPersonalDetails(PersonalDetailsFilter filter, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        PersonalDetailsSpecifications specification = PersonalDetailsSpecifications.builder()
                .firstName(filter.getFirstName())
                .lastName(filter.getLastName())
                .startDate(filter.getStartDate())
                .endDate(filter.getEndDate())
                .cityId(filter.getCityId())
                .stateId(filter.getStateId())
                .employmentStatusId(filter.getEmploymentStatusId())
                .build();


        var personalDetails = personalDetailsRepository.findAll(specification, pageable);

        var result = personalDetails.map(PersonalDetailsDto::mapToDto);

        return new PageResponse<PersonalDetailsDto>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast()
        );
    }
}
