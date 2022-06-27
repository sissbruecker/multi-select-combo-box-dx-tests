package com.example.application.data.generator;

import com.example.application.data.entity.Country;
import com.example.application.data.entity.SoftwareUpdate;
import com.example.application.data.service.CountryRepository;
import com.example.application.data.service.SoftwareUpdateRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(CountryRepository countryRepository, SoftwareUpdateRepository softwareUpdateRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (countryRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            ExampleDataGenerator<Country> countryGenerator = new ExampleDataGenerator<>(
                    Country.class, LocalDateTime.of(2022, 6, 27, 0, 0, 0));
            countryGenerator.setData(Country::setName, DataType.COUNTRY);
            countryRepository.saveAll(countryGenerator.create(10, seed));

            ExampleDataGenerator<SoftwareUpdate> softwareUpdateGenerator = new ExampleDataGenerator<>(SoftwareUpdate.class, LocalDateTime.of(2022, 6, 27, 0, 0, 0));
            softwareUpdateGenerator.setData(SoftwareUpdate::setVersion, DataType.EAN13);
            softwareUpdateGenerator.setData(SoftwareUpdate::setReleaseDate, DataType.DATE_NEXT_1_YEAR);
            softwareUpdateRepository.saveAll(softwareUpdateGenerator.create(10, seed));

            logger.info("Generated demo data");
        };
    }

}