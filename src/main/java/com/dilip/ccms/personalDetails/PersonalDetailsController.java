package com.dilip.ccms.personalDetails;

import com.dilip.ccms.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/personalDetails")
@RequiredArgsConstructor
public class PersonalDetailsController {

    private final Logger logger = LoggerFactory.getLogger(PersonalDetailsController.class);
    private final PersonalDetailsService personalDetailsService;


    // create
    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody PersonalDetailsDto personalDetailsDto, Authentication connectedUser) {
        try {
            var personalDetails = personalDetailsService.createPersonalDetails(personalDetailsDto, connectedUser);
            return ResponseHandler.generateResponse("Personal Details Added", HttpStatus.CREATED, personalDetails);
        } catch (Exception e) {
            logger.error("Failed to create personal details: {}", e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    /* retrieve
    findById():
    findAll():
    findFieldName():
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        try {
            var personalDetails = personalDetailsService.getPersonalDetailsById(id);
            return ResponseHandler.generateResponse("PersonalDetails fetched successfully", HttpStatus.OK, personalDetails);
        } catch (Exception e) {
            logger.error("Failed to fetch personal details with id {}", id, e);
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestParam(name = "status", required = false) int status,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        try {
            var personalDetailsList = personalDetailsService.getAllPersonalDetails(page, size, status);
            return ResponseHandler.generateResponse("PersonalDetails fetched successfully", HttpStatus.OK, personalDetailsList);
        } catch (Exception e) {
            logger.error("Failed to fetch personal details", e);
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    // update
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePersonalDetails(
            @PathVariable Integer id,
            @Valid @RequestBody PersonalDetailsDto request) {
        try {
            PersonalDetailsInfo updated = personalDetailsService.updatePersonalDetails(id, request);
            return ResponseHandler.generateResponse("Personal Details Update successfully", HttpStatus.OK, updated);
        } catch (Exception e) {
            logger.error("Failed to fetch personal details", e);
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePersonalDetails(@PathVariable Integer id) {
        try {
            boolean deleted = personalDetailsService.deletePersonalDetails(id);
            if (deleted)
                return ResponseHandler.generateResponse("Successfully deleted personalDetails with id" + id, HttpStatus.OK, id);
            else
                return ResponseHandler.generateResponse("Failed to delete personal", HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            logger.error("Failed to perform delete");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

//    @PostMapping("/filtered")
//    public ResponseEntity<Object> getPersonalDetailsFitered(
//            @RequestBody PersonalDetailsFilter filter,
//            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
//            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
//        try {
//            var personalDetails = personalDetailsService.getFilteredDetails(filter, page, size);
//            return ResponseHandler.generateResponse(
//                    "Successfully retrieved personal details using filter ",
//                    HttpStatus.OK,
//                    personalDetails);
//        }
//        catch (Exception e) {
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
//        }
//    }

    // filtered paginated and sorted
    @PostMapping("/search")
    public ResponseEntity<Object> search(@RequestBody PersonalDetailsFilter filter,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id") String sortBy){
        try{
            var personalDetails = personalDetailsService.searchPersonalDetails(filter, page, size, sortBy);
            return ResponseHandler.generateResponse("Retrieved details successfully", HttpStatus.OK, personalDetails);
        }
        catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


}
