package com.dilip.ccms.util;

import com.dilip.ccms.address.DataSeeder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppInitializer implements CommandLineRunner {


    private final DataSeeder dataSeeder;

    @Override
    public void run(String... args) throws Exception {
        dataSeeder.seedStateCityDataWithVirtualThreads();
        dataSeeder.seedTransactionCategoryData();
        dataSeeder.seedTransactionTypeDate();
    }
}
