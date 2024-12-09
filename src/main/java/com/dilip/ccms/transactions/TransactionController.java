package com.dilip.ccms.transactions;

import com.dilip.ccms.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable Integer id){
        try{
            var transaction = transactionService.findTransaction(id);
            return ResponseHandler.generateResponse("Successfully found transaction", HttpStatus.OK, transaction);
        }catch(NoSuchElementException e){
            return ResponseHandler.generateResponse("Transaction not found", HttpStatus.BAD_REQUEST, null);
        }catch (Exception e){
            return ResponseHandler.generateResponse("Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createTransaction(TransactionDto dto) {
        try{
            var transaction = transactionService.createTransaction(dto);
            return ResponseHandler.generateResponse("Successfully created transaction", HttpStatus.OK, transaction);
        }catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getTransactions(){
        try{
            var transactions = transactionService.getTransactions();
            return ResponseHandler.generateResponse("Successfully retrieved transactions", HttpStatus.OK, transactions);
        }catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getFilteredTransactions(
            @RequestBody TransactionFilter filter,
            @PathVariable Integer size,
            @PathVariable Integer page){
        try{
            var transactions = transactionService.getFilteredTransactions(filter, page, size);
            return ResponseHandler.generateResponse("Successfully retrieved transactions", HttpStatus.OK, transactions);
        }catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
