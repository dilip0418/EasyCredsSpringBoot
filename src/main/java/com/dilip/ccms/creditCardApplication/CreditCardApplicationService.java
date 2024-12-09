package com.dilip.ccms.creditCardApplication;

import com.dilip.ccms.crediCard.CreditCardService;
import com.dilip.ccms.exception.ApplicationAlreadyExistsException;
import com.dilip.ccms.exception.CreditCardAlreadyExistsException;
import com.dilip.ccms.personalDetails.PersonalDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CreditCardApplicationService {

    private final CreditCardApplicationRepository creditCardApplicationRepository;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final ApplicationStatusRepository applicationStatusRepository;
    private final CreditCardService creditCardService;

    public CreditCardApplication getCreditCardApplication(Integer id) {
        return creditCardApplicationRepository.findById(id).orElseThrow(); // if not found throws NoSuchElementException

    }

    public List<CreditCardApplication> getCreditCardApplications() {
        var creditCardApplications = creditCardApplicationRepository.findAll();
        if (creditCardApplications.isEmpty()) {
            throw new RuntimeException("No credit card applications found");
        } else {
            return creditCardApplications;
        }
    }

    public CreditCardApplication createApplication(CreditCardApplicationDto application)
            throws ApplicationAlreadyExistsException, CreditCardAlreadyExistsException {
        var existingApplication = creditCardApplicationRepository.findCreditCardApplicationByPersonalDetails_Id(application.getApplicantId());
        if (existingApplication.isPresent()) {
            var applicationStatus = existingApplication.get().getApplicationStatus().getId();
            if (applicationStatus == 2 || applicationStatus == 4)
                throw new ApplicationAlreadyExistsException("You already have an application in progress");
            else if (applicationStatus == 3)
                throw new CreditCardAlreadyExistsException("You already have a credit card");
        }
        var personalDetails = personalDetailsRepository.findById(application.getApplicantId()).orElseThrow();
        var applicationStatus = applicationStatusRepository.findById(2).orElseThrow(); // Saved status
        CreditCardApplication newApplication = new CreditCardApplication();
        newApplication.setApplicationDate(LocalDate.now());
        newApplication.setLastModifiedDate(LocalDate.now());
        newApplication.setPersonalDetails(personalDetails);
        newApplication.setEmail(application.getEmail());
        newApplication.setPhone(application.getPhone());
        newApplication.setApplicationStatus(applicationStatus);
        newApplication.setAnnualIncome(application.getAnnualIncome());
        newApplication.setComments("");

        return creditCardApplicationRepository.save(newApplication);
    }

    public CreditCardApplication updateApplicationStatus(Integer applicationId, ApplicationStatusUpdateRequest request)
            throws NoSuchFieldException {
        var application = this.getCreditCardApplication(applicationId);
        if (application == null) {
            throw new NoSuchFieldException("Application not found");
        }

        var applicationStatus = applicationStatusRepository.findById(request.getStatusId()).orElseThrow();
        var result = creditCardApplicationRepository.updateApplicationStatusAndCommentsBy(applicationStatus, request.getComments());
        if (result > 0) {
            if (application.getApplicationStatus().getId() == 3) // application accepted
                creditCardService.createCreditCardAsync(CreditCardApplicationDto.toDto(application));
            else if (application.getApplicationStatus().getId() == 4) // application rejected
            {
                // TODO: Send email to the user with message as credit card application got cancelled.
            }
            return application;
        } else {
            throw new RuntimeException("Couldn't update application status");
        }
    }

    public CreditCardApplication updateApplication(CreditCardApplicationDto dto) throws NoSuchFieldException {
        var application = this.getCreditCardApplication(dto.getId());

        if (application == null) {
            throw new NoSuchFieldException("Application not found");
        }

        application.setLastModifiedDate(LocalDate.now());
        application.setAnnualIncome(dto.getAnnualIncome());
        application.setEmail(dto.getEmail());
        application.setPhone(dto.getPhone());
        application.setComments("");

        return creditCardApplicationRepository.save(application);
    }

    public void deleteApplication(Integer id) {
        if (!creditCardApplicationRepository.existsById(id)) {
            throw new NoSuchElementException("Credit card not found");
        }
        creditCardApplicationRepository.deleteById(id);
    }

}
