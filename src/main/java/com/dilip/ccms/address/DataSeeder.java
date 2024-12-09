package com.dilip.ccms.address;

import com.dilip.ccms.transactions.TransactionCategory;
import com.dilip.ccms.transactions.TransactionCategoryRepository;
import com.dilip.ccms.transactions.TransactionType;
import com.dilip.ccms.transactions.TransactionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class DataSeeder {

    private final AddressService addressService;

    private final StateRepository stateRepository;

    private final CityRepository cityRepository;

    private final TransactionCategoryRepository transactionCategoryRepository;

    private final TransactionTypeRepository transactionTypeRepository;

    // Add ExecutorService for concurrent running operations
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    // Add Logger from slf4j
    private final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    public void seedStateCityDataWithVirtualThreads() {
        if (stateRepository.count() == 0)
            this.fetchAndSaveStates();
        if (cityRepository.count() == 0)
            this.fetchAndSaveCities();
    }

    private void fetchAndSaveStates() {
        List<State> states;
        try {
            states = addressService.fetchStatesFromAPI();
            logger.info("Fetched {} from address api", states.size());
            stateRepository.saveAll(states);
        } catch (Exception e) {
            logger.warn("Failed to fetch states from address api", e);
            throw new RuntimeException(e);
        }
    }

    private void fetchAndSaveCities() {
/*
      synchronous approach
        try {
            states.forEach(state -> {
                List<City> cities = addressService.fetchCitiesFromAPI(state.getStateCode());
                cities.forEach(city -> city.setState(state));
                cityRepository.saveAll(cities);
            });
            logger.info("cities saved successfully to the database from api");

        } catch (Exception e) {
            logger.warn("Failed to fetch cities from api", e);
            throw new RuntimeException(e);
        }
*/
        // asynchronous approach

        List<State> states = stateRepository.findAll();

        try {
            List<CompletableFuture<Void>> futures = states.stream()
                    .map(state -> CompletableFuture.supplyAsync(() -> addressService.fetchCitiesFromAPI(state.getStateCode()),
                                    executorService)
                            .thenAccept(cities -> {
                                cities.forEach(city -> city.setState(state));
                                cityRepository.saveAll(cities);
                            })
                            .exceptionally(e -> {
                                logger.warn("Failed to fetch cities for state {}", state.getName());
                                return null;
                            }))
                    .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            logger.info("Cities saved successfully to the database from API");
        } catch (Exception e) {
            logger.error("Failed to process cities", e);
        } finally {
            executorService.shutdown();
        }


    }

    @Async
    public void seedTransactionCategoryData(){
        if(transactionCategoryRepository.count() == 0){
        List<TransactionCategory> categories = new ArrayList<>();
            categories.add(new TransactionCategory(1, "Groceries"));
            categories.add(new TransactionCategory(2, "Travel"));
            categories.add(new TransactionCategory(3, "Entertainment"));
            categories.add(new TransactionCategory(4, "Utilities"));
            categories.add(new TransactionCategory(5, "Health"));
            categories.add(new TransactionCategory(6, "Education"));
            categories.add(new TransactionCategory(7, "Shopping"));
            categories.add(new TransactionCategory(8, "Dining"));
            categories.add(new TransactionCategory(9, "Fuel"));
            categories.add(new TransactionCategory(10, "Rent"));
            transactionCategoryRepository.saveAll(categories);
        }
    }

    @Async
    public void seedTransactionTypeDate(){
        if(transactionTypeRepository.count() == 0){
        List<TransactionType> types = new ArrayList<>();
            types.add(new TransactionType(1, "Payment"));
            types.add(new TransactionType(2, "Purchase"));
            transactionTypeRepository.saveAll(types);
        }
    }
}
