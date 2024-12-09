package com.dilip.ccms.personalDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Integer>,
        JpaSpecificationExecutor<PersonalDetails> {

    @Query(value = """
            SELECT DISTINCT pd FROM PersonalDetails pd
            LEFT JOIN pd.address a
            LEFT JOIN a.city c
            LEFT JOIN a.state s
            LEFT JOIN pd.employmentStatus es
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT pd)
                    FROM PersonalDetails pd
                    """)
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "address",
                    "address.city",
                    "address.state",
                    "employmentStatus"
            }
    )
    Page<PersonalDetailsInfo> findProjectedBy(Pageable pageable);

    Page<PersonalDetailsInfo> findProjectedByEmploymentStatus_Id(int Id, Pageable pageable);

    Optional<PersonalDetailsInfo> findProjectedById(Integer id);
}
