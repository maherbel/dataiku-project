package com.dataiku.millenium.business;

import com.dataiku.millenium.business.records.PathOptimizerTestData;
import com.dataiku.millenium.entities.Route;
import com.dataiku.millenium.pojos.EmpireModel;
import com.dataiku.millenium.pojos.MilleniumFalconModel;
import com.dataiku.millenium.pojos.MissionResultModel;
import com.dataiku.millenium.pojos.Planet;
import com.dataiku.millenium.utils.MockBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PathOptimizerTest {

    @ParameterizedTest
    @MethodSource("missionInputProvider")
    public void computeMissionResult(PathOptimizerTestData pathOptimizerTestData) {
        // Create mock EmpireModel object
        EmpireModel empireModel = pathOptimizerTestData.getEmpireModel();

        // Create mock Route objects and mock the service call
        List<Route> routes = pathOptimizerTestData.getRoutes();

        // Create the MilleniumFalconModel and mock the injected bean methods
        MilleniumFalconModel milleniumFalconModel = pathOptimizerTestData.getMilleniumFalconModel();

        // Invoke the computeMissionResult method
        PathOptimizer pathOptimizer = new PathOptimizer();
        MissionResultModel result = pathOptimizer.computeMissionResult(empireModel, routes, milleniumFalconModel);

        // Verify the expected success probability
        assertEquals(pathOptimizerTestData.getExpectedSuccessProbability(), result.getMissionSuccessProbability());
    }

    @Test
    public void testComputeEmpireLocations() {
        // create test input
        MockBuilder mockBuilder = new MockBuilder();
        EmpireModel empireModel = mockBuilder.buildEmpireModel(
                Arrays.asList(
                        new Planet("Tatooine", 1),
                        new Planet("Hoth", 3),
                        new Planet("Endor", 5)));

        // call method under test
        PathOptimizer pathOptimizer = new PathOptimizer();
        Map<String, List<Integer>> empireLocations = pathOptimizer.computeEmpireLocations(empireModel);

        // verify results
        assertEquals(3, empireLocations.size());
        assertTrue(empireLocations.containsKey("Tatooine"));
        assertTrue(empireLocations.containsKey("Hoth"));
        assertTrue(empireLocations.containsKey("Endor"));
        assertEquals(1, empireLocations.get("Tatooine").size());
        assertEquals(1, empireLocations.get("Hoth").size());
        assertEquals(1, empireLocations.get("Endor").size());
        assertEquals(1, (int) empireLocations.get("Tatooine").get(0));
        assertEquals(3, (int) empireLocations.get("Hoth").get(0));
        assertEquals(5, (int) empireLocations.get("Endor").get(0));
    }

    @Test
    public void testComputeNeighboursByPlanet() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("Tatooine", "Hoth", 5));
        routes.add(new Route("Tatooine", "Coruscant", 8));
        routes.add(new Route("Coruscant", "Tatooine", 9));
        routes.add(new Route("Hoth", "Coruscant", 2));

        Map<String, Planet> planetHelper = new HashMap<>();
        PathOptimizer pathOptimizer = new PathOptimizer();
        Map<Planet, Map<Planet, Integer>> neighboursByPlanet = pathOptimizer.computeNeighboursByPlanet(routes, planetHelper);

        assertEquals(3, neighboursByPlanet.size());
        assertTrue(neighboursByPlanet.containsKey(planetHelper.get("Tatooine")));
        assertTrue(neighboursByPlanet.containsKey(planetHelper.get("Hoth")));
        assertTrue(neighboursByPlanet.containsKey(planetHelper.get("Coruscant")));

        // Check Tatooine neighbours
        Map<Planet, Integer> tatooineNeighbours = neighboursByPlanet.get(planetHelper.get("Tatooine"));
        assertEquals(2, tatooineNeighbours.size());
        // Test that Tatooine is a neighbour of both Hoth and Coruscant, with correct travel times
        assertTrue(tatooineNeighbours.containsKey(planetHelper.get("Hoth")));
        assertEquals(Integer.valueOf(5), tatooineNeighbours.get(planetHelper.get("Hoth")));
        assertTrue(tatooineNeighbours.containsKey(planetHelper.get("Coruscant")));
        assertEquals(Integer.valueOf(8), tatooineNeighbours.get(planetHelper.get("Coruscant")));

        // Check Hoth neighbours
        Map<Planet, Integer> hothNeighbours = neighboursByPlanet.get(planetHelper.get("Hoth"));
        assertEquals(2, hothNeighbours.size());
        // Test that Hoth is a neighbour of both Tatooine and Coruscant, with correct travel times
        assertTrue(hothNeighbours.containsKey(planetHelper.get("Tatooine")));
        assertEquals(Integer.valueOf(5), hothNeighbours.get(planetHelper.get("Tatooine")));
        assertTrue(hothNeighbours.containsKey(planetHelper.get("Coruscant")));
        assertEquals(Integer.valueOf(2), hothNeighbours.get(planetHelper.get("Coruscant")));

        // Check Coruscant neighbours
        Map<Planet, Integer> coruscantNeighbours = neighboursByPlanet.get(planetHelper.get("Coruscant"));
        assertEquals(2, coruscantNeighbours.size());
        // Test that Coruscant is a neighbour of both Tatooine and Coruscant, with correct travel times
        assertTrue(coruscantNeighbours.containsKey(planetHelper.get("Hoth")));
        assertEquals(Integer.valueOf(2), coruscantNeighbours.get(planetHelper.get("Hoth")));
        assertTrue(coruscantNeighbours.containsKey(planetHelper.get("Tatooine")));
        assertEquals(Integer.valueOf(8), coruscantNeighbours.get(planetHelper.get("Tatooine")));
    }

    @Test
    public void testRefuelAutonomy() {
        // Test that refueling happens when necessary
        int autonomyLeft = 50;
        int milleniumFalconAutonomy = 100;
        Planet currentPlanet = new Planet("Tatooine", 0, false);
        List<Planet> path = new ArrayList<>();
        path.add(currentPlanet);
        int travelTimeToNeighbour = 200;

        // Create a PathOptimizer
        PathOptimizer pathOptimizer = new PathOptimizer();
        int remainingAutonomy = pathOptimizer.refuelAutonomy(autonomyLeft, milleniumFalconAutonomy, currentPlanet, path, travelTimeToNeighbour);

        // Assert refueling
        assertTrue(remainingAutonomy > autonomyLeft); // Autonomy increased due to refueling
        assertEquals(remainingAutonomy, 250); // Autonomy have increased twice (2 * 100)
        assertEquals(3, path.size()); // Refueling added two days to the path
        assertEquals("Tatooine", path.get(2).getName()); // Refueling happened at Tatooine
        assertEquals(2, path.get(2).getDay()); // Refueling took two days
        assertTrue(path.get(2).isRefuel());
    }

    @Test
    public void testComputeRiskyPlanets() {
        // setup test data
        Map<String, List<Integer>> empireLocations = new HashMap<>();
        empireLocations.put("Tatooine", Arrays.asList(0, 1, 2, 3, 4));
        empireLocations.put("Hoth", Arrays.asList(1, 2, 3, 4, 5));
        List<Planet> path = new ArrayList<>();
        path.add(new Planet("Tatooine", 0));
        path.add(new Planet("Hoth", 1));
        path.add(new Planet("Endor", 2));

        // invoke method under test
        PathOptimizer pathOptimizer = new PathOptimizer();
        List<Planet> riskyPlanets = pathOptimizer.computeRiskyPlanets(empireLocations, path);

        // verify results
        assertEquals(2, riskyPlanets.size());
        assertEquals("Tatooine", riskyPlanets.get(0).getName());
        assertEquals(0, riskyPlanets.get(0).getDay());
        assertEquals("Hoth", riskyPlanets.get(1).getName());
        assertEquals(1, riskyPlanets.get(1).getDay());
    }

    @Test
    public void testIsBetterPath() {
        // Create a list of risky planets
        List<Planet> riskyPlanets1 = Arrays.asList(new Planet(), new Planet());
        List<Planet> riskyPlanets2 = Arrays.asList(new Planet(), new Planet());

        // Create a list of planets for the first path
        List<Planet> path1 = new ArrayList<>();
        path1.add(new Planet("Tatooine", 0, false));
        path1.add(new Planet("Naboo", 4, true));
        path1.add(new Planet("Hoth", 9, false));

        // Create a list of planets for the second path
        List<Planet> path2 = new ArrayList<>();
        path2.add(new Planet("Tatooine", 0, false));
        path2.add(new Planet("Naboo", 5, true));
        path2.add(new Planet("Hoth", 7, false));

        // Create a PathOptimizer
        PathOptimizer pathOptimizer = new PathOptimizer();
        // Set the initial best path
        pathOptimizer.setMinRiskyPlanets(3);
        pathOptimizer.setBestPath(Arrays.asList(new Planet("Tatooine", 0, false),
                new Planet("Naboo", 2, false),
                new Planet("Tatooine", 8, false)));

        // Path1 is considered a better path than the current best path because fewer risky planets
        assertTrue(pathOptimizer.isBetterPath(riskyPlanets1, path1));

        // Path2 is considered a better path than the current best path because arrives at last day earlier
        assertTrue(pathOptimizer.isBetterPath(riskyPlanets2, path2));
    }

    @Test
    public void testCalculateSuccessProbability() {
        PathOptimizer pathOptimizer = new PathOptimizer();
        pathOptimizer.setMinRiskyPlanets(0);
        assertEquals(100.0, pathOptimizer.calculateSuccessProbability(), 0.01);

        pathOptimizer.setMinRiskyPlanets(1);
        assertEquals(90.0, pathOptimizer.calculateSuccessProbability(), 0.01);

        pathOptimizer.setMinRiskyPlanets(2);
        assertEquals(81.0, pathOptimizer.calculateSuccessProbability(), 0.01);

        pathOptimizer.setMinRiskyPlanets(3);
        assertEquals(72.9, pathOptimizer.calculateSuccessProbability(), 0.01);
    }

    static Stream<PathOptimizerTestData> missionInputProvider() {
        MockBuilder mockBuilder = new MockBuilder();
        // TODO ADD EDGE CASES SUCH AS WRONG INPUT DATA, EMPTY, INCORRECT, INCOMPLETE, ETC..
        return Stream.of(
                new PathOptimizerTestData(
                        mockBuilder.buildEmpireModel(7,
                                Arrays.asList(
                                        new Planet("Hoth", 4),
                                        new Planet("Hoth", 5),
                                        new Planet("Hoth", 6))),
                        Arrays.asList(
                                new Route("Tatooine", "Dagobah", 6),
                                new Route("Dagobah", "Endor", 4),
                                new Route("Dagobah", "Hoth", 1),
                                new Route("Hoth", "Endor", 1),
                                new Route("Tatooine", "Hoth", 6)
                        ),
                        mockBuilder.buildMilleniumFalconModel(6, "Tatooine", "Endor"),
                        0),
                new PathOptimizerTestData(
                        mockBuilder.buildEmpireModel(8,
                                Arrays.asList(
                                        new Planet("Hoth", 6),
                                        new Planet("Hoth", 7),
                                        new Planet("Hoth", 8))),
                        Arrays.asList(
                                new Route("Tatooine", "Dagobah", 6),
                                new Route("Dagobah", "Endor", 4),
                                new Route("Dagobah", "Hoth", 1),
                                new Route("Hoth", "Endor", 1),
                                new Route("Tatooine", "Hoth", 6)
                        ),
                        mockBuilder.buildMilleniumFalconModel(6, "Tatooine", "Endor"),
                        81.00
                ),
                new PathOptimizerTestData(
                        mockBuilder.buildEmpireModel(9,
                                Arrays.asList(
                                        new Planet("Hoth", 6),
                                        new Planet("Hoth", 7),
                                        new Planet("Hoth", 8))),
                        Arrays.asList(
                                new Route("Tatooine", "Dagobah", 6),
                                new Route("Dagobah", "Endor", 4),
                                new Route("Dagobah", "Hoth", 1),
                                new Route("Hoth", "Endor", 1),
                                new Route("Tatooine", "Hoth", 6)
                        ),
                        mockBuilder.buildMilleniumFalconModel(6, "Tatooine", "Endor"),
                        90
                ),
                new PathOptimizerTestData(
                        mockBuilder.buildEmpireModel(10,
                                Arrays.asList(
                                        new Planet("Hoth", 6),
                                        new Planet("Hoth", 7),
                                        new Planet("Hoth", 8))),
                        Arrays.asList(
                                new Route("Tatooine", "Dagobah", 6),
                                new Route("Dagobah", "Endor", 4),
                                new Route("Dagobah", "Hoth", 1),
                                new Route("Hoth", "Endor", 1),
                                new Route("Tatooine", "Hoth", 6)
                        ),
                        mockBuilder.buildMilleniumFalconModel(6, "Tatooine", "Endor"),
                        100
                ),
                new PathOptimizerTestData(
                        mockBuilder.buildEmpireModel(6,
                                Arrays.asList(
                                        new Planet("Hoth", 2),
                                        new Planet("Star", 3),
                                        new Planet("Venus", 4))),
                        Arrays.asList(
                                new Route("Tatooine", "Dagobah", 1),
                                new Route("Dagobah", "Hoth", 1),
                                new Route("Hoth", "Star", 1),
                                new Route("Star", "Venus", 1),
                                new Route("Venus", "Endor", 1)
                        ),
                        mockBuilder.buildMilleniumFalconModel(4, "Tatooine", "Endor"),
                        100
                ),
                new PathOptimizerTestData(
                        mockBuilder.buildEmpireModel(11,
                                Arrays.asList(
                                        new Planet("Venus", 5),
                                        new Planet("Venus", 7),
                                        new Planet("Venus", 8),
                                        new Planet("Venus", 9),
                                        new Planet("Pluton", 5),
                                        new Planet("Pluton", 6),
                                        new Planet("Pluton", 7),
                                        new Planet("Pluton", 8),
                                        new Planet("Pluton", 9),
                                        new Planet("Moon", 5),
                                        new Planet("Moon", 6),
                                        new Planet("Moon", 7),
                                        new Planet("Moon", 8),
                                        new Planet("Moon", 9),
                                        new Planet("Earth", 5),
                                        new Planet("Earth", 6),
                                        new Planet("Earth", 7),
                                        new Planet("Earth", 8),
                                        new Planet("Earth", 9))),
                        Arrays.asList(
                                new Route("Tatooine", "Dagobah", 1),
                                new Route("Dagobah", "Hoth", 1),
                                new Route("Hoth", "Star", 1),
                                new Route("Star", "Venus", 1),
                                new Route("Venus", "Pluton", 1),
                                new Route("Pluton", "Moon", 1),
                                new Route("Moon", "Earth", 1),
                                new Route("Earth", "Endor", 1)
                        ),
                        mockBuilder.buildMilleniumFalconModel(3, "Tatooine", "Endor"),
                        81
                ),
                new PathOptimizerTestData(
                        mockBuilder.buildEmpireModel(7,
                                Arrays.asList(
                                        new Planet("Dagobah", 3),
                                        new Planet("Dagobah", 4),
                                        new Planet("Endor", 6),
                                        new Planet("Hoth", 5),
                                        new Planet("Hoth", 6))),
                        Arrays.asList(
                                new Route("Tatooine", "Dagobah", 2),
                                new Route("Tatooine", "Hoth", 5),
                                new Route("Dagobah", "Endor", 2),
                                new Route("Dagobah", "Tatooine", 2),
                                new Route("Hoth", "Endor", 1)
                        ),
                        mockBuilder.buildMilleniumFalconModel(3, "Tatooine", "Endor"),
                        100)
        );

    }
}