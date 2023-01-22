package com.dataiku.millenium.services;

import com.dataiku.millenium.exceptions.ComputeMissionResultException;
import com.dataiku.millenium.exceptions.MissionDataRetrievalException;
import com.dataiku.millenium.pojos.*;
import com.dataiku.millenium.repositories.RouteRepository;
import com.dataiku.millenium.business.PathOptimizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * This service provides methods for computing mission results and retrieving mission data.
 * <p>
 * It uses a RouteRepository to access route information, a MilleniumFalconModel to get information
 * about the Millenium Falcon, and a PathOptimizer to compute optimal routes and mission results.
 */
@Service
public class RouteService {

    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);
    /**
     * The repository for accessing route information.
     */
    final RouteRepository routeRepository;
    /**
     * The model for the Millenium Falcon.
     */
    final MilleniumFalconModel milleniumFalconModel;
    /**
     * The utility for computing optimal routes.
     */
    final PathOptimizer pathOptimizer;


    /**
     * Constructs a new RouteService with the given route repository, Millenium Falcon model, and path optimizer.
     *
     * @param routeRepository      The repository for accessing route information.
     * @param milleniumFalconModel The model for the Millenium Falcon.
     * @param pathOptimizer        The utility for computing optimal routes.
     */
    public RouteService(RouteRepository routeRepository, MilleniumFalconModel milleniumFalconModel, PathOptimizer pathOptimizer) {
        this.routeRepository = routeRepository;
        this.milleniumFalconModel = milleniumFalconModel;
        this.pathOptimizer = pathOptimizer;
    }

    /**
     * Computes the mission result for the given empire data.
     *
     * @param empireModel The model for the empire data.
     * @return The mission result.
     */
    public MissionResultModel computeMissionResult(EmpireModel empireModel) throws ComputeMissionResultException {
        try {
            MissionResultModel missionResultModel = pathOptimizer.computeMissionResult(empireModel, routeRepository.findAll(), this.milleniumFalconModel);
            logger.info("Compute mission result ended with a success probability of [{}%].", missionResultModel.getMissionSuccessProbability());
            return missionResultModel;
        } catch (Exception e) {
            logger.error("An exception occurred during mission result computation..", e);
            throw new ComputeMissionResultException("An error occurred while computing mission result", e);
        }
    }

    /**
     * Retrieves data for the current mission.
     * <p>
     * The mission data includes the autonomy of the Millenium Falcon, the departure and arrival planets,
     * and a map of routes between planets.
     *
     * @return The mission data.
     */
    public MissionDataModel getMissionData() throws MissionDataRetrievalException {
        try {
            Map<String, Planet> planetHelper = new HashMap<>();
            Map<Planet, Map<Planet, Integer>> routesAdjacencyMap = pathOptimizer.computeNeighboursByPlanet(routeRepository.findAll(), planetHelper);
            MissionDataModel missionDataModel = new MissionDataModel();
            missionDataModel.setAutonomy(this.milleniumFalconModel.getAutonomy());
            missionDataModel.setDeparture(this.milleniumFalconModel.getDeparture());
            missionDataModel.setArrival(this.milleniumFalconModel.getArrival());
            missionDataModel.setNodes(routesAdjacencyMap);
            logger.info("Retrieved mission data: {}", missionDataModel);
            return missionDataModel;
        } catch (Exception e) {
            logger.error("An exception occurred during mission data retrieval..", e);
            throw new MissionDataRetrievalException("An error occurred while retrieving mission data", e);
        }
    }
}
