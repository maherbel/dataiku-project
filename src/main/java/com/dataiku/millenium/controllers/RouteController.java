package com.dataiku.millenium.controllers;

import com.dataiku.millenium.exceptions.ComputeMissionResultException;
import com.dataiku.millenium.exceptions.MissionDataRetrievalException;
import com.dataiku.millenium.pojos.EmpireModel;
import com.dataiku.millenium.pojos.ErrorModel;
import com.dataiku.millenium.pojos.MissionDataModel;
import com.dataiku.millenium.pojos.MissionResultModel;
import com.dataiku.millenium.services.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles requests for the route computation section of the application.
 * <p>
 * It provides methods for:
 * -Checking the App status through a healthcheck
 * -Requesting the Mission Falcon data
 * -Compute the Millenium Falcon mission result
 */
@RestController
@RequestMapping("/")
public class RouteController {

    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);

    /**
     * The service for managing the routes
     */
    private final RouteService routeService;

    /**
     * Constructs a new RouteController with the given route service.
     *
     * @param routeService The service for managing routes
     */
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * Handles a GET request the App healthcheck.
     *
     * @return A string representing the availability of the App.
     */
    @GetMapping("/healthcheck")
    public String healthCheck() {
        logger.info("Health check endpoint accessed.");
        return "OK";
    }

    /**
     * Handlers a GET request to retrieve the Mission data.
     *
     * @return A MissionDataModel
     */
    @GetMapping("/missionData")
    public ResponseEntity<Object> getMissionData() {
        logger.info("Retrieving mission data.");
        try {
            logger.info("Retrieving mission data.");
            return ResponseEntity.ok(routeService.getMissionData());
        } catch (Exception | MissionDataRetrievalException e) {
            logger.error("An error happened during the mission data retrieval.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorModel(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    /**
     * Handles a POST request to compute the Millenium Falcon mission success probability.
     * This probability is a percentage that can range from 0% to 100%.
     *
     * @param empireModel The empire data containing the mission countdown and the bounty hunters positions
     * @return A MissionResultModel containing the mission success probability and the relative Planets path if any
     */
    @PostMapping(value = "/missionResultSuccess", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> computeMilleniumFalconMission(@RequestBody EmpireModel empireModel) {
        try {
            logger.info("Computing mission success probability.");
            return ResponseEntity.ok(routeService.computeMissionResult(empireModel));
        } catch (Exception | ComputeMissionResultException e) {
            logger.error("An error happened during the mission result compute.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorModel(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
