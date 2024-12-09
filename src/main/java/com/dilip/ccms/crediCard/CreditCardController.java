package com.dilip.ccms.crediCard;

import com.dilip.ccms.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CreditCardController {
    private final CreditCardService cardService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCreditCard(@PathVariable Integer id) {
        try {
            return ResponseHandler.generateResponse("Successfully retrieved credit card", HttpStatus.OK, cardService.getCreditCard(id));
        } catch (NoSuchElementException ne) {
            return ResponseHandler.generateResponse(ne.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (DataAccessException de) {
            return ResponseHandler.generateResponse(de.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping()
    public ResponseEntity<Object> getCreditCards() {
        try {
            var creditCards = cardService.getCreditCards();
            if (creditCards.isEmpty())
                return ResponseHandler.generateResponse("No credit cards were found", HttpStatus.NO_CONTENT, null);
            return ResponseHandler.generateResponse("Successfully retrieved credit cards", HttpStatus.OK, creditCards);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Error while retrieving credit cards", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCreditCards(@PathVariable Integer id) {
        try {
            cardService.deleteCard(id);
            return ResponseHandler.generateResponse("Successfully deleted credit card", HttpStatus.NO_CONTENT, "Deleted");
        } catch (NoSuchElementException ne) {
            return ResponseHandler.generateResponse(ne.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Failed to perform delete operation", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateCard(@RequestBody CreditCardDto dto) {
        try {
            var creditCard = cardService.updateCreditCard(dto);
            return ResponseHandler.generateResponse("Successfully updated credit card", HttpStatus.OK, creditCard);
        }
        catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // As credit card is automatically created once the application is accepted, so we are not exposing the create endpoint
}
