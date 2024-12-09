package com.dilip.ccms.creditCardApplication;

import com.dilip.ccms.exception.ApplicationAlreadyExistsException;
import com.dilip.ccms.exception.CreditCardAlreadyExistsException;
import com.dilip.ccms.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class CreditCardApplicationController {
    private final CreditCardApplicationService creditCardApplicationService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCreditCardApplication(@PathVariable Integer id) {
        try {
            var application = creditCardApplicationService.getCreditCardApplication(id);
            return ResponseHandler.generateResponse("Retrieved application Successfully", HttpStatus.NO_CONTENT, application);
        } catch (NoSuchElementException ne) {
            return ResponseHandler.generateResponse(ne.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (DataAccessException de) {
            return ResponseHandler.generateResponse(de.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getCreditCardApplications() {
        try {
            var applications = creditCardApplicationService.getCreditCardApplications();
            if (applications.isEmpty()) {
                return ResponseHandler.generateResponse("No Applications found", HttpStatus.NO_CONTENT, null);
            }
            return ResponseHandler.generateResponse("Retrieved " + applications.size() + " applications", HttpStatus.OK, applications);
        }catch (Exception e){
            return ResponseHandler.generateResponse("Failed to fetch applications",HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createApplication(CreditCardApplicationDto application){
        try{
            var newApplication = creditCardApplicationService.createApplication(application);
            return ResponseHandler.generateResponse("Created application Successfully", HttpStatus.OK, newApplication);
        } catch (CreditCardAlreadyExistsException | ApplicationAlreadyExistsException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateApplicationStatus(
            @PathVariable Integer id,
            @RequestBody ApplicationStatusUpdateRequest request){
        try{
            var application = creditCardApplicationService.updateApplicationStatus(id,request);
            return ResponseHandler.generateResponse("Updated application Status", HttpStatus.OK, application);
        } catch (NoSuchFieldException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NO_CONTENT, null);
        }catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCreditCardApplication(@PathVariable Integer id) {
        try {
            creditCardApplicationService.deleteApplication(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error"); // 500 Internal Server Error
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request"); // 400 Bad Request
        }
    }
}
