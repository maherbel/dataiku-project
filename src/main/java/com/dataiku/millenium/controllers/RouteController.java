package com.dataiku.millenium.controllers;

import com.dataiku.millenium.pojos.EmpireModel;
import com.dataiku.millenium.pojos.MissionDataModel;
import com.dataiku.millenium.pojos.MissionResultModel;
import com.dataiku.millenium.services.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public MissionDataModel getMissionData() {
        logger.info("Retrieving mission data.");
        return routeService.getMissionData();
    }

    /**
     * Handles a POST request to compute the Millenium Falcon mission success probability.
     *
     * @param page The page number to retrieve (optional, default is 0).
     * @param size The number of users per page (optional, default is 10).
     * @return A list of users.
     */
    /**
     * Handles a POST request to compute the Millenium Falcon mission success probability.
     * This probability is a percentage that can range from 0% to 100%.
     *
     * @param empireModel The empire data containing the mission countdown and the bounty hunters positions
     * @return A MissionResultModel containing the mission success probability and the relative Planets path if any
     */
    @PostMapping(value = "/missionResultSuccess", consumes = "application/json", produces = "application/json")
    public MissionResultModel computeMilleniumFalconMission(@RequestBody EmpireModel empireModel) {
        // TODO ADD ERROR INSIDE MISSIONRESULTMODEL TO RETURN TO CLIENT IF PROCESS FAILED
        logger.info("Computing mission success probability.");
        return routeService.computeMissionResult(empireModel);
    }
}
