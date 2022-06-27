package com.example.application.data.generator;

import com.example.application.data.entity.SoftwareUpdate;
import com.example.application.data.entity.TeslaModel;
import com.example.application.data.service.TeslaModelRepository;
import com.example.application.data.service.SoftwareUpdateRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(TeslaModelRepository teslaModelRepository, SoftwareUpdateRepository softwareUpdateRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (teslaModelRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            TeslaModel modelS = new TeslaModel();
            modelS.setName("Model S");
            teslaModelRepository.save(modelS);

            TeslaModel modelX = new TeslaModel();
            modelX.setName("Model X");
            teslaModelRepository.save(modelX);

            TeslaModel model3 = new TeslaModel();
            model3.setName("Model 3");
            teslaModelRepository.save(model3);

            TeslaModel modelY = new TeslaModel();
            modelY.setName("Model Y");
            teslaModelRepository.save(modelY);

            ExampleDataGenerator<SoftwareUpdate> softwareUpdateGenerator = new ExampleDataGenerator<>(SoftwareUpdate.class, LocalDateTime.of(2022, 6, 27, 0, 0, 0));
            softwareUpdateGenerator.setData(SoftwareUpdate::setVersion, DataType.EAN13);
            softwareUpdateGenerator.setData(SoftwareUpdate::setReleaseDate, DataType.DATE_NEXT_1_YEAR);
            softwareUpdateRepository.saveAll(softwareUpdateGenerator.create(10, seed));

            logger.info("Generated demo data");
        };
    }

}