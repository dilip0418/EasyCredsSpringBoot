package com.dilip.ccms.transactions;

import jakarta.persistence.criteria.Predicate;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionSpecifications {
    public static Specification<Transaction> byCriteria(
            BigDecimal startAmount, BigDecimal endAmount,
            LocalDate startDate, LocalDate endDate,
            Integer categoryId, Integer typeId) {

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // Filter by startAmount only if it's provided
            if (startAmount != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), startAmount));
            }

            // Filter by endAmount only if it's provided
            if (endAmount != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("amount"), endAmount));
            }

            // Filter by startDate only if it's provided
            if (startDate != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), startDate));
            }

            // Filter by endDate only if it's provided
            if (endDate != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), endDate));
            }

            // Filter by categoryId only if it's provided
            if (categoryId != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            // Filter by typeId only if it's provided
            if (typeId != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("type").get("id"), typeId));
            }

            // Return all records if no criteria are provided
            return predicate;
        };
    }
}
