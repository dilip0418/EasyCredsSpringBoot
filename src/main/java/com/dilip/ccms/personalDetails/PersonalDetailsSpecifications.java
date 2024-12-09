package com.dilip.ccms.personalDetails;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalDetailsSpecifications implements Specification<PersonalDetails>{

    private String firstName;
    private String lastName;
    private LocalDate endDate;
    private LocalDate startDate;
    private Integer cityId;
    private Integer stateId;
    private Integer employmentStatusId;

//    public static Specification<PersonalDetails> byFilter(PersonalDetailsFilter filter) {
//        return (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            if (filter.getFirstName() != null) {
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + filter.getFirstName().toLowerCase() + "%"));
//            }
//            if (filter.getLastName() != null) {
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + filter.getLastName().toLowerCase() + "%"));
//            }
//            if (filter.getDateOfBirth() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("dateOfBirth"), filter.getDateOfBirth()));
//            }
//            if (filter.getCityId() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("address").get("city").get("id"), filter.getCityId()));
//            }
//            if (filter.getStateId() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("address").get("state").get("id"), filter.getStateId()));
//            }
//            if (filter.getEmploymentStatusId() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("employmentStatus").get("id"), filter.getEmploymentStatusId()));
//            }
//            if (filter.getMinAnnualIncome() != null && !(filter.getMinAnnualIncome().compareTo(BigDecimal.ZERO) < 0)) {
//                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("annualIncome"), filter.getMinAnnualIncome()));
//            }
//            if (filter.getMaxAnnualIncome() != null && !(filter.getMaxAnnualIncome().compareTo(BigDecimal.ZERO) < 0)) {
//                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("annualIncome"), filter.getMaxAnnualIncome()));
//            }
//            return criteriaBuilder.and(predicates.toArray(new Predicate[0])); // Use AND here
//        };
//    }


    @Override
    public Predicate toPredicate(Root<PersonalDetails> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"));
        }
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%"));
        }
        if (startDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"), startDate));
        }
        if (endDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateOfBirth"), endDate));
        }

        if (cityId != null) {
            predicates.add(criteriaBuilder.equal(root.join("address").get("city").get("id"), cityId));
        }
        if (stateId != null) {
            predicates.add(criteriaBuilder.equal(root.join("address").get("state").get("id"), stateId));
        }
        if (employmentStatusId != null) {
            predicates.add(criteriaBuilder.equal(root.join("employmentStatus").get("id"), employmentStatusId));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}


