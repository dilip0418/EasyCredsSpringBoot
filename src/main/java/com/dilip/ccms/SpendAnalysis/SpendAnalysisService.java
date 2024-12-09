package com.dilip.ccms.SpendAnalysis;

import com.dilip.ccms.transactions.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpendAnalysisService {

    private final SpendAnalysisRepository spendAnalysisRepository;

    @Transactional
    public void updateSpendAnalysis(Transaction transaction) {
        // Extract data from Transaction
        Integer userId = transaction.getCreditCard().getPersonalDetails().getId(); // Assuming userId is in PersonalDetails
        Integer categoryId = transaction.getCategory().getId();
        BigDecimal amount = transaction.getAmount();
        LocalDate date = transaction.getTransactionDate();
        int month = date.getMonthValue();
        int year = date.getYear();

        // Check if a SpendAnalysis record already exists
        spendAnalysisRepository.findByUserIdAndCategoryIdAndMonthAndYear(userId, categoryId, month, year)
                .ifPresentOrElse(
                        spendAnalysis -> {
                            // Update existing record
                            spendAnalysis.setTotalSpend(spendAnalysis.getTotalSpend().add(amount));
                            spendAnalysisRepository.save(spendAnalysis);
                        },
                        () -> {
                            // Insert a new record
                            SpendAnalysis newAnalysis = new SpendAnalysis();
                            newAnalysis.setUserId(userId);
                            newAnalysis.setCategoryId(categoryId);
                            newAnalysis.setTotalSpend(amount);
                            newAnalysis.setMonth(month);
                            newAnalysis.setYear(year);
                            spendAnalysisRepository.save(newAnalysis);
                        }
                );
    }

    public List<MonthlyCategorySpend> getMonthlyCategorySpend(Integer userId, Integer months) {
        LocalDate startDate = LocalDate.now().minusMonths(months);
        return spendAnalysisRepository.findByUserIdAndDateRange(userId, startDate.getYear(), startDate.getMonthValue())
                .stream()
                .map(sa -> new MonthlyCategorySpend(
                        sa.getCategoryId(),
                        sa.getMonth(),
                        sa.getTotalSpend()
                ))
                .collect(Collectors.toList());
    }

    public List<MonthlySpend> getYearlySpendingTrend(Integer userId, Integer year) {
        return spendAnalysisRepository.findByUserIdAndYear(userId, year)
                .stream()
                .collect(Collectors.groupingBy(SpendAnalysis::getMonth,
                        Collectors.mapping(  // Normalizing the amount values
                                SpendAnalysis::getTotalSpend,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet()
                .stream()
                .map(entry -> new MonthlySpend(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(MonthlySpend::getMonth))
                .collect(Collectors.toList());
    }


    public List<CategoryBreakdown> getCategoryBreakdown(Integer userId, Integer year, Integer month) {
        List<SpendAnalysis> spendList = spendAnalysisRepository.findByUserIdAndMonthAndYear(userId, month, year);
        BigDecimal totalSpend = spendList.stream()
                .map(SpendAnalysis::getTotalSpend)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Normalizing the amount

        return spendList.stream()
                .map(sa -> new CategoryBreakdown(
                        sa.getCategoryId(),
                        sa.getTotalSpend(),
                        sa.getTotalSpend()
                                .divide(totalSpend, 2, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100))
                                .doubleValue() // percent calculation
                ))
                .collect(Collectors.toList());
    }
}
