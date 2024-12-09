package com.dilip.ccms.transactions;

import com.dilip.ccms.crediCard.CreditCardRepository;
import com.dilip.ccms.creditCardApplication.CreditCardApplicationRepository;
import com.dilip.ccms.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final CreditCardRepository creditCardRepository;

    public Transaction findTransaction(Integer id){
        return transactionRepository.findById(id).orElseThrow();
    }

    public List<Transaction> getTransactions(){
        return transactionRepository.findAll();
    }

    // Paginated filtered transactions
    public PageResponse<Transaction> getFilteredTransactions(TransactionFilter filter, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

        Page<Transaction> transactions;

        Specification<Transaction> specification = TransactionSpecifications.byCriteria(
                filter.getStartAmount(),filter.getEndAmount(),
                filter.getStartDate(),filter.getEndDate(),
                filter.getCategoryId(), filter.getTypeId()
        );

        transactions = transactionRepository.findAll(specification, pageable);

        return new PageResponse<>(
                transactions.getContent(),
                transactions.getNumber(),
                transactions.getSize(),
                transactions.getTotalElements(),
                transactions.getTotalPages(),
                transactions.isFirst(),
                transactions.isLast()
        );
    }

    public Transaction createTransaction(TransactionDto dto) {
        var category = transactionCategoryRepository.findById(dto.getCategoryId()).orElseThrow();
        var type = transactionTypeRepository.findById(dto.getTypeId()).orElseThrow();
        var creditCard = creditCardRepository.findById(dto.getCreditCardId()).orElseThrow();

        var transaction = Transaction.builder()
                .transactionDate(LocalDate.now())
                .amount(dto.getAmount())
                .category(category)
                .type(type)
                .creditCard(creditCard)
                .build();

        return transactionRepository.save(transaction);
    }
}
