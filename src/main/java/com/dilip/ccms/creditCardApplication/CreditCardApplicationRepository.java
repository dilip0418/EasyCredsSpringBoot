package com.dilip.ccms.creditCardApplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CreditCardApplicationRepository extends JpaRepository<CreditCardApplication, Integer>  {

    Optional<CreditCardApplication> findCreditCardApplicationByPersonalDetails_Id(Integer personalDetailsId);

    @Transactional
    @Modifying
    @Query("update CreditCardApplication c set c.applicationStatus = ?1, c.comments = ?2")
    int updateApplicationStatusAndCommentsBy(ApplicationStatus applicationStatus, String comments);

}
