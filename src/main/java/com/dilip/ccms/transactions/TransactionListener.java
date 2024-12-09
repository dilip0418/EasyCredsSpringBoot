package com.dilip.ccms.transactions;

import com.dilip.ccms.SpendAnalysis.SpendAnalysisService;
import jakarta.persistence.PostPersist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionListener {

    private final SpendAnalysisService spendAnalysisService;

    @PostPersist
    public void afterInsert(Transaction transaction) {
        spendAnalysisService.updateSpendAnalysis(transaction);
    }
}

