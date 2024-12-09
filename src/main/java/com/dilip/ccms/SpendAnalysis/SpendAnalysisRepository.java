package com.dilip.ccms.SpendAnalysis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpendAnalysisRepository extends JpaRepository<SpendAnalysis, Integer> {

    Optional<SpendAnalysis> findByUserIdAndCategoryIdAndMonthAndYear(
            Integer userId, Integer categoryId, Integer month, Integer year);

    @Query("SELECT sa FROM SpendAnalysis sa WHERE sa.userId = :userId AND sa.year = :year")
    List<SpendAnalysis> findByUserIdAndYear(@Param("userId") Integer userId, @Param("year") Integer year);

    @Query("SELECT sa FROM SpendAnalysis sa WHERE sa.userId = :userId AND sa.year = :year AND sa.month = :month")
    List<SpendAnalysis> findByUserIdAndMonthAndYear(@Param("userId") Integer userId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT sa FROM SpendAnalysis sa WHERE sa.userId = :userId AND (sa.year > :startYear OR (sa.year = :startYear AND sa.month >= :startMonth))")
    List<SpendAnalysis> findByUserIdAndDateRange(@Param("userId") Integer userId, @Param("startYear") Integer startYear, @Param("startMonth") Integer startMonth);
}
