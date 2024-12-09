package com.dilip.ccms.address;

import com.dilip.ccms.user.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Value(("${application.address-api.API}"))
    private String API_URL;

    @Value("${application.address-api.state-api}")
    private String stateEndpoint;

    @Value("${application.address-api.city-api}")
    private String cityEndpoint;

    @Value("${application.address-api.API_HOST}")
    private String API_HOST;

    @Value("${application.address-api.API_KEY}")
    private String API_KEY;


    public Address findAddressById(Integer id) {
        return addressRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    public Integer saveAddress(Address address, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        return 1;
    }

    public List<State> fetchStatesFromAPI() throws Exception {
        String STATE_URL = API_URL + stateEndpoint + "?countrycode=in";

        var headers = this.getHttpHeaders();

        // Create an entity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the API call
        ResponseEntity<String> response = restTemplate.exchange(STATE_URL, HttpMethod.GET, entity, String.class);

        // Get the response body as a String
        String responseBody = Objects.requireNonNull(response.getBody());

        // Parse the response using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        List<State> states = objectMapper.readValue(responseBody, new TypeReference<List<State>>() {
        });

        // Define allowed states for filtering
        List<String> allowedStates = Arrays.asList("Karnataka", "Tamil Nadu", "Andhra Pradesh", "Telangana", "Kerala");

        // Filter the allowed states and return the result
        return states.stream()
                .filter(state -> allowedStates.contains(state.getName()))
                .toList();
    }


    public List<City> fetchCitiesFromAPI(String stateCode) {
        String CITY_URL = API_URL + cityEndpoint + "?countrycode=in&statecode=" + stateCode.toLowerCase();

        var headers = this.getHttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        logger.info("Sending request to: {}", CITY_URL);
        ResponseEntity<City[]> response = restTemplate.exchange(CITY_URL, HttpMethod.GET, entity, City[].class);
        logger.info("Response obtained");
        return List.of(Objects.requireNonNull(response.getBody()));
    }

    // Set headers
    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-host", API_HOST);
        headers.set("x-rapidapi-key", API_KEY);
        return headers;
    }
}
