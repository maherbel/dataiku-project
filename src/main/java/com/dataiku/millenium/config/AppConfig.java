package com.dataiku.millenium.config;

import com.dataiku.millenium.pojos.EmpireModel;
import com.dataiku.millenium.pojos.MilleniumFalconModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 * Configuration class that defines beans for the Millenium Falcon and Empire models.
 * The path to the JSON file containing the model data can be specified using a command line argument or
 * through the application property 'millenium.falcon.data.file.path'.
 */
@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value(value = "${millenium.falcon.data.file.path}")
    private String milleniumFalconJsonFilePath;

    private final ApplicationArguments applicationArguments;

    /**
     * Constructor that injects the {@link ApplicationArguments} instance.
     *
     * @param applicationArguments the instance of {@link ApplicationArguments}
     */
    public AppConfig(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    /**
     * Bean definition for the {@link MilleniumFalconModel} object.
     * The path to the JSON file containing the model data can be specified using a command line argument or
     * through the application property 'millenium.falcon.data.file.path'.
     *
     * @return the {@link MilleniumFalconModel} object
     * @throws IOException if an error occurs while reading the JSON file
     */
    @Bean(name = "milleniumFalconModelBean")
    public MilleniumFalconModel getMilleniumFalconModel() throws IOException {
        // check if the command line argument was passed
        String[] configFilePathArgs = applicationArguments.getSourceArgs();
        if (configFilePathArgs != null && configFilePathArgs.length >= 1) {
            // use the value of the command line argument as the path to the JSON file
            if (Paths.get(configFilePathArgs[0]).isAbsolute()) {
                milleniumFalconJsonFilePath = configFilePathArgs[0];
            } else {
                milleniumFalconJsonFilePath = "examples/" + configFilePathArgs[0];
            }
        }
        logger.debug("Loading Millenium Falcon model from {}", milleniumFalconJsonFilePath);
        // read the JSON file and parse its content
        ObjectMapper objectMapper = new ObjectMapper();
        // inputStream to read the file from resources and disk
        InputStream inputStream;
        if (Paths.get(milleniumFalconJsonFilePath).isAbsolute()) {
            inputStream = new FileInputStream(milleniumFalconJsonFilePath);
        } else {
            Resource resource = new ClassPathResource(milleniumFalconJsonFilePath);
            inputStream = resource.getInputStream();
        }
        MilleniumFalconModel model = objectMapper.readValue(inputStream, MilleniumFalconModel.class);
        logger.info("Millenium falcon model loaded: {}", model);
        return model;
    }

    /**
     * Bean definition for the {@link EmpireModel} object.
     * The path to the JSON file containing the model data is read from the command line arguments.
     * If the command line argument was not provided, this bean will return a null value.
     *
     * @return the {@link EmpireModel} object, or null if the command line argument was not provided
     * @throws IOException if an error occurs while reading the JSON file
     */
    @Bean(name = "empireModelBean")
    public EmpireModel getEmpireModel() throws IOException {
        // check if the command line argument was passed
        String[] configFilePathArgs = applicationArguments.getSourceArgs();
        if (configFilePathArgs != null && configFilePathArgs.length == 2) {
            // use the value of the command line argument as the path to the JSON file
            String empireModelFilePath = configFilePathArgs[1];
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream;
            logger.info("Empire model JSON file path: {}", empireModelFilePath);
            if (Paths.get(empireModelFilePath).isAbsolute()) {
                inputStream = new FileInputStream(empireModelFilePath);
            } else {
                Resource resource = new ClassPathResource("examples/" + empireModelFilePath);
                inputStream = resource.getInputStream();
            }
            EmpireModel empireModel = objectMapper.readValue(inputStream, EmpireModel.class);
            logger.info("Millenium falcon model loaded: {}", empireModel);
            return empireModel;
        } else {
            return null;
        }
    }

}
