package com.dataiku.millenium.business;

import com.dataiku.millenium.entities.Route;
import com.dataiku.millenium.pojos.EmpireModel;
import com.dataiku.millenium.pojos.MilleniumFalconModel;
import com.dataiku.millenium.pojos.MissionResultModel;
import com.dataiku.millenium.pojos.Planet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

@Component
@Scope(value = "prototype", proxyMode = TARGET_CLASS)
public class PathOptimizer {

    private static final Logger logger = LoggerFactory.getLogger(PathOptimizer.class);

    private Integer minRiskyPlanets = Integer.MAX_VALUE;

    public final int STARTING_DAY = 0;

    private List<Planet> bestPath = new ArrayList<>();

    /**
     * Computes the mission result based on the given {@link EmpireModel} and the list of available {@link Route}s.
     * The result includes the success probability of the mission and the list of {@link Planet}s that will be visited
     * in the optimal path.
     *
     * @param empireModel          the model of the Empire's movements and locations
     * @param routes               the list of available routes for the mission
     * @param milleniumFalconModel the model of the Millenium Falcon
     * @return the mission result, including success probability and path
     */
    public MissionResultModel computeMissionResult(EmpireModel empireModel, List<Route> routes, MilleniumFalconModel milleniumFalconModel) {
        logger.info("Compute mission result started. Departure: [{}], Arrival: [{}], Countdown: [{}]",
                milleniumFalconModel.getDeparture(), milleniumFalconModel.getArrival(), empireModel.getCountdown());
        // Compute the different routes (from planet to planet) for how much travel time and create a Map of Planet by planet name
        Map<String, Planet> planetHelper = new HashMap<>();
        Map<Planet, Map<Planet, Integer>> routesAdjacencyMap = computeNeighboursByPlanet(routes, planetHelper);
        // Compute the empire locations as for each planet which days they will be there
        Map<String, List<Integer>> empireLocations = computeEmpireLocations(empireModel);
        // Fetch the best possible route (if exists) with the best probability to not get caught by the Empire
        List<List<Planet>> allPossiblePaths = computePaths(empireModel, routesAdjacencyMap, planetHelper, milleniumFalconModel);
        // Process all successful paths (if any) that were found given the constraints
        MissionResultModel missionResultModel = computeBestSuccessProbability(allPossiblePaths, empireModel, empireLocations);
        logger.info("Compute mission result ended with a success probability of [{}%].", missionResultModel.getMissionSuccessProbability());
        return missionResultModel;
    }

    /**
     * Utils Code Block
     */

    /**
     * This method computes the neighbours for each planet in a graph based on the given routes.
     * It returns a map where the key is the origin planet and the value is a map of its neighbours.
     * The inner map contains the destination planet as the key and the travel time as the value.
     * If there are multiple routes between the same two planets, the one with the smallest travel time will be used.
     *
     * @param routes       list of routes containing information on the origin, destination, and travel time for each route
     * @param planetHelper map of planets keyed by their name to be used as a helper for creating the graph
     * @return a map of planet to map of its neighbours with travel time as the value
     */
    public Map<Planet, Map<Planet, Integer>> computeNeighboursByPlanet(List<Route> routes, Map<String, Planet> planetHelper) {
        Map<Planet, Map<Planet, Integer>> neighboursByPlanet = new HashMap<>();
        for (Route route : routes) {
            String origin = route.getOrigin();
            String dest = route.getDestination();
            Integer travelTime = route.getTravelTime();
            planetHelper.computeIfAbsent(origin, k -> new Planet(origin));
            planetHelper.computeIfAbsent(dest, k -> new Planet(dest));
            Planet originPlanet = planetHelper.get(origin);
            Planet destPlanet = planetHelper.get(dest);
            /**
             * The two following lines are responsible for adding the neighbours in each Map
             * relatively to the route's origin/dest/travelTime to make sure the bidirectional edge is correctly added
             * Also, if there is multiple routes between the same two nodes, the one with the smallest travelTime will be added
             */
            neighboursByPlanet.computeIfAbsent(originPlanet, k -> new HashMap<>())
                    .compute(destPlanet, (k, oldTravelTime) -> oldTravelTime == null || travelTime < oldTravelTime ? travelTime : oldTravelTime);
            neighboursByPlanet.computeIfAbsent(destPlanet, k -> new HashMap<>())
                    .compute(originPlanet, (k, oldTravelTime) -> oldTravelTime == null || travelTime < oldTravelTime ? travelTime : oldTravelTime);
        }
        return neighboursByPlanet;
    }

    /**
     * Computes a map of planets and the days that the Empire's bounty hunters will be present on each planet.
     *
     * @param empireModel the {@link EmpireModel} object containing information about the Empire's bounty hunters movements
     * @return a map of planet names and lists of days the Empire will be present on each planet
     */
    public Map<String, List<Integer>> computeEmpireLocations(EmpireModel empireModel) {
        Map<String, List<Integer>> empireLocations = new HashMap<>();
        for (Planet planet : empireModel.getBountyHunters()) {
            empireLocations.computeIfAbsent(planet.getName(), k -> new ArrayList<>()).add(planet.getDay());
        }
        return empireLocations;
    }

    /**
     * Utils Code Block
     */


    /**
     * Path Finding Code Block
     */

    /**
     * Computes all the possible paths from a departure planet to an arrival planet. This means that the Falcon is able
     * to get to the arrival planet before/at the countdown with a success probability greater than 0
     * The method uses a DFS approach to traverse all possible paths while keeping a Visited Set to avoid cycles
     * by not going through Planets already traversed
     *
     * @param empireModel:          Data about the bounty hunters
     * @param neighbours:           Adjacency Map of the planets and the travel time between them
     * @param planetHelper:         Helper to get the Planet given a planet name
     * @param milleniumFalconModel: Data about the millenium falcon
     * @return
     */
    public List<List<Planet>> computePaths(EmpireModel empireModel, Map<Planet, Map<Planet, Integer>> neighbours, Map<String, Planet> planetHelper, MilleniumFalconModel milleniumFalconModel) {
        // Get the needed infos about the mission
        Integer autonomy = milleniumFalconModel.getAutonomy();
        String departure = milleniumFalconModel.getDeparture();
        String arrival = milleniumFalconModel.getArrival();
        Integer countdown = empireModel.getCountdown();
        // List for storing the paths
        List<List<Planet>> paths = new ArrayList<>();
        // Set for storing the visited nodes
        Set<String> visited = new HashSet<>();
        // Start the search at the start node and find all paths that leads to end node
        Planet departurePlanet = planetHelper.get(departure);
        Planet arrivalPlanet = planetHelper.get(arrival);
        // Set the Starting day in the departure planet to 0
        departurePlanet.setDay(STARTING_DAY);
        // Call the recursive DFS traversal method
        visitNeighbours(countdown, autonomy, 0, departurePlanet, departurePlanet, arrivalPlanet, new ArrayList<>(), paths, neighbours, visited, milleniumFalconModel);
        // Return all paths
        logger.info("{} Possible paths found.", paths.size());
        return paths;
    }

    /**
     * Recursive helper function for traversing recursively each planet's neighbours
     *
     * @param countdown
     * @param autonomyLeft
     * @param travelTime
     * @param previousPlanet
     * @param currentPlanet
     * @param arrival
     * @param path
     * @param paths
     * @param neighbours
     * @param visited
     * @param milleniumFalconModel
     */
    public void visitNeighbours(int countdown, int autonomyLeft, int travelTime, Planet previousPlanet, Planet currentPlanet, Planet arrival, List<Planet> path, List<List<Planet>> paths, Map<Planet, Map<Planet, Integer>> neighbours, Set<String> visited, MilleniumFalconModel milleniumFalconModel) {
        // Going to this planet will take more time than the countdown allows so we can skip it in the current path
        // This does not mean we won't go by this planet (could be there a path that allows going through this planet later)
        if (previousPlanet.getDay() + travelTime > countdown) {
            return;
        }
        // Update the current planet day
        currentPlanet.setDay(previousPlanet.getDay() + travelTime);
        currentPlanet.setRefuel(false);
        // Add the current planet to the path
        path.add(currentPlanet.deepCopy());
        // Update the autonomy left
        autonomyLeft -= travelTime;
        // Mark the node as visited
        visited.add(currentPlanet.getName());

        if (currentPlanet.equals(arrival)) {
            // If the node is the goal, add the path to the list
            paths.add(new ArrayList<>(path));
        } else {
            // If the node is not the goal, explore its neighbourPlanets
            Map<Planet, Integer> neighbourPlanets = neighbours.get(currentPlanet);
            for (Planet neighbourPlanet : neighbourPlanets.keySet()) {
                if (!visited.contains(neighbourPlanet.getName())) {
                    // Need to refuel by staying for an extra day in the current planet before going to neighbour
                    Integer travelTimeToneighbour = neighbourPlanets.get(neighbourPlanet);
                    if (travelTimeToneighbour > autonomyLeft) {
                        autonomyLeft = refuelAutonomy(autonomyLeft, milleniumFalconModel.getAutonomy(), currentPlanet, path, travelTimeToneighbour);
                    }
                    visitNeighbours(countdown, autonomyLeft, travelTimeToneighbour, currentPlanet, neighbourPlanet,
                            arrival, path, paths, neighbours, visited, milleniumFalconModel);
                }
            }
        }
        // Remove the refuel planets
        while (path.get(path.size() - 1).isRefuel()) path.remove(path.size() - 1);
        // Remove the node from the path, mark it as unvisited and reset its Day value as it may be visited again
        path.remove(path.size() - 1);
        visited.remove(currentPlanet.getName());
        currentPlanet.setDay(null);
    }

    /**
     * Refuel autonomy of the Falcon to continue traversing planets
     *
     * @param autonomyLeft
     * @param milleniumFalconAutonomy
     * @param currentPlanet
     * @param path
     * @param travelTimeToneighbour
     * @return
     */
    public int refuelAutonomy(int autonomyLeft, int milleniumFalconAutonomy, Planet currentPlanet, List<Planet> path, Integer travelTimeToneighbour) {
        while (travelTimeToneighbour > autonomyLeft) {
            currentPlanet.setDay(currentPlanet.getDay() + 1);
            currentPlanet.setRefuel(true);
            path.add(currentPlanet.deepCopy());
            autonomyLeft += milleniumFalconAutonomy;
        }
        return autonomyLeft;
    }

    /**
     * Path Finding Code Block
     */

    /**
     * This method determines if the current Falcon Path from departure to arrival goes through Planets/Days
     * on which the bounty hunters are present and stores the number of risky planets and the falcon path if
     * it is the best outcome so far.
     *
     * @param empireLocations: Map contains the Days per Planet where the bounty hunters are present
     * @param path:            Current successful Path that is being evaluated against the current best solution
     * @return
     */
    public List<Planet> computeRiskyPlanets(Map<String, List<Integer>> empireLocations, List<Planet> path) {
        List<Planet> commonElements = new ArrayList<>();
        for (Planet planet : path) {
            if (empireLocations.containsKey(planet.getName()) && empireLocations.get(planet.getName()).contains(planet.getDay())) {
                commonElements.add(planet);
            }
        }
        if (isBetterPath(commonElements, path)) {
            this.minRiskyPlanets = commonElements.size();
            this.bestPath = new ArrayList<>(path.size());
            for (Planet planet : path) {
                this.bestPath.add(planet.deepCopy());
            }
            Planet arrivalPlanet = this.bestPath.get(this.bestPath.size()-1);
            logger.info("[{}] is the best path so far as it has [{}] risky positions and arrives on [{}] on Day [{}].",
                    this.bestPath, this.minRiskyPlanets, arrivalPlanet.getName(), arrivalPlanet.getDay());
        }
        return commonElements;
    }

    /**
     * The method will rate a path as "best solution" if the current risky planets are less than the existing best solution
     * or if the number of risky planets is the same and the new path gets to the arrival planet earlier
     * than the current best path.
     *
     * @param riskyPlanets
     * @param path
     * @return
     */
    boolean isBetterPath(List<Planet> riskyPlanets, List<Planet> path) {
        int riskyPlanetNumber = riskyPlanets.size();
        return riskyPlanetNumber < this.minRiskyPlanets ||
                (riskyPlanetNumber == this.minRiskyPlanets && path.get(path.size() - 1).getDay() < this.bestPath.get(this.bestPath.size() - 1).getDay());
    }

    /**
     * Calculates the success probability of the mission using the minimal number of risky planets
     *
     * @return
     */
    public double calculateSuccessProbability() {
        double sum = 0;
        for (int i = 0; i < this.minRiskyPlanets; i++) {
            sum += Math.pow(9, i) / Math.pow(10, i + 1);
        }
        return 100 - (sum * 100);
    }

    /**
     * Computes the mission result given all the successful paths that allows the falcon to go from departure to arrival
     * and the empire data such as countdown and bounty hunters positions
     *
     * @param successfulPaths
     * @param empireModel
     * @param empireLocations
     * @return
     */
    public MissionResultModel computeBestSuccessProbability(List<List<Planet>> successfulPaths, EmpireModel empireModel, Map<String, List<Integer>> empireLocations) {
        MissionResultModel missionResultModel = new MissionResultModel();
        missionResultModel.setMissionSuccessProbability(0);
        missionResultModel.setMissionPath(null);
        if (!successfulPaths.isEmpty()) {
            for (List<Planet> path : successfulPaths) {
                Double pathSuccessProbability = computePathSuccessProbability(path, empireModel, empireLocations);
                if (pathSuccessProbability > missionResultModel.getMissionSuccessProbability()) {
                    missionResultModel.setMissionSuccessProbability(pathSuccessProbability);
                }
            }
        }
        missionResultModel.setMissionPath(this.bestPath);
        return missionResultModel;
    }

    /**
     * In this method, the refuel points (might be multiple) are swapped for all possible combinations
     * to determine the best possible solution in terms of pathing/riskyPlanets.
     * The algorithm approach is backtracking through all possibilities by swapping backwards (all planets that
     * comes before a one on which a refuel is done) for the farthest point in the path.
     * For every possible position of these swaps, the earliest refuels will also go through the same swam
     * mechanism on each and every leftsome planets.
     *
     * @param path
     * @param refuelIndices
     * @param refuelIndex
     * @param empireLocations
     * @param spareDays
     */
    public void refuelSooner(List<Planet> path, List<Integer> refuelIndices, int refuelIndex, Map<String, List<Integer>> empireLocations, int spareDays) {
        if (refuelIndex < 0) return;

        Integer refuelPlanetIndex = refuelIndices.get(refuelIndex);
        for (int i = refuelPlanetIndex - 2; i > 0; i--) {
            // Refuel sooner at the Planet placed at index i
            swapRefuelStep(path, refuelPlanetIndex, i);
            // Check risky planets and days when bounty hunters might be present
            computeRiskyPlanets(empireLocations, path).size();
            // Wait for more days at each location (from 1 to spareDays for each time in every planet)
            delayAllRoutes(path, empireLocations, spareDays);
            // Refuel sooner for earlier refuel points while taking into account the current refuel swap
            refuelSooner(path, refuelIndices, refuelIndex - 1, empireLocations, spareDays);
            // Revert the current swap
            revertSwapRefuel(path, i + 1, refuelPlanetIndex);
        }
    }

    /**
     * Since the Falcon can get to the arrival Planet before the countdown, there might be some extra days left
     * that could actually be used to extend the stay in each Planet for This method allows to test every combaison
     * by staying 1 to spareDays in all planets for each iteration
     *
     * @param path
     * @param empireLocations
     * @param spareDays
     */
    public void delayAllRoutes(List<Planet> path, Map<String, List<Integer>> empireLocations, int spareDays) {
        if (spareDays == 0) return;
        // The outer loop here starts at 1 and ends at advanceDays because we want to add wait one more day at each
        // planet in the path until we wait the total of advanceDays
        // The inner loop here starts at 1 because we cannot start in the departure planet at Day 1 (it should be 0)
        for (int i = 1; i < path.size(); i++) {
            for (int day = 1; day <= spareDays; day++) {
                for (int j = i; j < path.size(); j++) {
                    path.get(j).setDay(path.get(j).getDay() + 1);
                }
                path.get(i).setDelay(day);
                computeRiskyPlanets(empireLocations, path).size();
            }

            // As explained above, the loop here starts from 1 because the departure planet should always be on Day 0
            for (int j = i; j < path.size(); j++) {
                path.get(j).setDay(path.get(j).getDay() - spareDays);
            }
            path.get(i).setDelay(0);
        }
    }

    /**
     * This method serves to perform the refuel swap by :
     * - removing the current refuel planet
     * - putting the new refuel planet by cloning the correct planet
     * - update the planets in between as the day has to increase by 1
     *
     * @param path
     * @param refuelPlanetIndex
     * @param newRefuelPlanetIndex
     */
    private void swapRefuelStep(List<Planet> path, int refuelPlanetIndex, int newRefuelPlanetIndex) {
        Planet newRefuelPlanet = path.get(newRefuelPlanetIndex).deepCopy();
        newRefuelPlanet.setDay(newRefuelPlanet.getDay() + 1);
        newRefuelPlanet.setRefuel(true);
        path.remove(refuelPlanetIndex);
        path.add(newRefuelPlanetIndex + 1, newRefuelPlanet);
        /*
            Since the planet in which we stayed for refuel is at newRefuelPlanetIndex
           This means that the refuel planet will be at newRefuelPlanetIndex + 1
           That also means that we need to impact all planets between newRefuelPlanetIndex + 2
           until the old refuel planet position (that have been removed and replaced in the path by its previous planet)
           The latest one to be updated is the old planet in which we stayed to do a refuel
        */
        for (int i = newRefuelPlanetIndex + 2; i <= refuelPlanetIndex; i++) {
            path.get(i).setDay(path.get(i).getDay() + 1);
        }
    }

    /**
     * This methods serves a revert of the refuel swap by :
     * - removing the current refuel planet
     * - putting back the old refuel planet
     * - update the planets in between as the day has to decrease by 1
     *
     * @param path
     * @param currentRefuelPlanetIndex
     * @param newRefuelPlanetIndex
     */
    private void revertSwapRefuel(List<Planet> path, int currentRefuelPlanetIndex, int newRefuelPlanetIndex) {
        Planet newRefuelPlanet = path.get(newRefuelPlanetIndex).deepCopy();
        path.get(newRefuelPlanetIndex).setRefuel(true);
        path.add(newRefuelPlanetIndex, newRefuelPlanet);
        path.remove(currentRefuelPlanetIndex);
        /*
            The idea here is to reset every node between the old refuel planet and new one to the correct day
            using an offset of -1
         */
        for (int i = newRefuelPlanetIndex - 1; i >= currentRefuelPlanetIndex; i--) {
            path.get(i).setDay(path.get(i).getDay() - 1);
        }
    }

    /**
     * Return the path success probability that will range from 0 to 100 and will represent a percentage.
     *
     * @param path
     * @param empireModel
     * @param empireLocations
     * @return
     */
    private Double computePathSuccessProbability(List<Planet> path, EmpireModel empireModel, Map<String, List<Integer>> empireLocations) {
        int countdown = empireModel.getCountdown();
        int arrivalDay = path.get(path.size() - 1).getDay();
        List<Planet> commonElements = computeRiskyPlanets(empireLocations, path);
        if (commonElements.isEmpty()) {
            return 100.00;
        } else {
            int advanceDays = countdown - arrivalDay;
            List<Integer> refuelIndices = IntStream.range(0, path.size())
                    .boxed()
                    .filter(i -> path.get(i).isRefuel())
                    .collect(Collectors.toCollection(ArrayList::new));
            refuelSooner(path, refuelIndices, refuelIndices.size() - 1, empireLocations, advanceDays);
            delayAllRoutes(path, empireLocations, advanceDays);
            return calculateSuccessProbability();
        }
    }

    public Integer getMinRiskyPlanets() {
        return minRiskyPlanets;
    }

    public void setMinRiskyPlanets(Integer minRiskyPlanets) {
        this.minRiskyPlanets = minRiskyPlanets;
    }

    public List<Planet> getBestPath() {
        return bestPath;
    }

    public void setBestPath(List<Planet> bestPath) {
        this.bestPath = bestPath;
    }
}
