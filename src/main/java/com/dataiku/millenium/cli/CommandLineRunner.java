package com.dataiku.millenium.cli;

import com.dataiku.millenium.exceptions.ComputeMissionResultException;
import com.dataiku.millenium.pojos.EmpireModel;
import com.dataiku.millenium.pojos.MilleniumFalconModel;
import com.dataiku.millenium.pojos.MissionResultModel;
import com.dataiku.millenium.pojos.Planet;
import com.dataiku.millenium.services.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * This component is run when the application starts, and it is responsible for computing the mission
 * result and printing it to the console.
 */
@Component
@DependsOn("empireModelBean")
public class CommandLineRunner implements ApplicationRunner, ApplicationContextAware {

    /**
     * The logger for the CommandLineRunner class
     */
    private static final Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);
    /**
     * The model for the Millenium Falcon.
     */
    final MilleniumFalconModel milleniumFalconModel;
    /**
     * The model for the empire data.
     */
    final EmpireModel empireModel;
    /**
     * The service for computing mission results.
     */
    private final RouteService routeService;
    /**
     * The application context.
     */
    private ApplicationContext applicationContext;

    /**
     * Constructs a new CommandLineRunner with the given Millenium Falcon model, empire model, and
     * route service.
     *
     * @param milleniumFalconModel The model for the Millenium Falcon.
     * @param empireModel          The model for the empire data.
     * @param routeService         The service for computing mission results.
     */
    public CommandLineRunner(MilleniumFalconModel milleniumFalconModel, EmpireModel empireModel, RouteService routeService) {
        this.milleniumFalconModel = milleniumFalconModel;
        this.empireModel = empireModel;
        this.routeService = routeService;
    }

    /**
     * Runs the application and computes the mission result.
     * <p>
     * If the empire model is not null, this method uses the route service to compute the mission
     * result, prints the result to the console, and exits the application.
     *
     * @param args The application arguments.
     */
    @Override
    public void run(ApplicationArguments args) {
        if (this.empireModel != null) {
            try {
                logger.info("Computing mission result from CLI..");
                MissionResultModel result = routeService.computeMissionResult(this.empireModel);
                // Log the mission result
                logger.info("****************** Results ******************");
                logger.info("Mission Success Probability: {}", result.getMissionSuccessProbability());
                logger.info("*********************************************");
            } catch (Exception | ComputeMissionResultException e) {
                logger.error("Unexpected error when computing result from CLI..", e);
            } finally {
                SpringApplication.exit(applicationContext, () -> 0);
            }
        } else {
            logger.warn("Empire model is null, CLI not available.");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
