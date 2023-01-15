/*package com.dataiku.millenium.services;

import com.dataiku.millenium.pojos.*;
import com.dataiku.millenium.repositories.RouteRepository;
import com.dataiku.millenium.business.PathOptimizer;
import com.dataiku.millenium.business.records.PathOptimizerTestData;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private MilleniumFalconModel milleniumFalconModel;

    @Autowired
    private PathOptimizer pathOptimizer;

    @InjectMocks
    private RouteService routeService;

    @ParameterizedTest
    @MethodSource("missionInputProvider")
    public void testComputeMissionResult(PathOptimizerTestData pathOptimizerTestData) {
        // Plug the real implementation of PathOptimizer
        when(routeService.pathOptimizer).thenReturn(pathOptimizer);

        // Create mock EmpireModel object
        EmpireModel empireModel = testData.empireModel();

        // Create mock Route objects and mock the service call
        List<Route> routes = testData.routes();
        when(routeRepository.findAll()).thenReturn(routes);

        // Create the MilleniumFalconModel and mock the injected bean methods
        MilleniumFalconModel milleniumFalconModelMock = testData.milleniumFalconModel();
        when(milleniumFalconModel.getAutonomy()).thenReturn(milleniumFalconModelMock.getAutonomy());
        when(milleniumFalconModel.getDeparture()).thenReturn(milleniumFalconModelMock.getDeparture());
        when(milleniumFalconModel.getArrival()).thenReturn(milleniumFalconModelMock.getArrival());

        // Invoke the computeMissionResult method
        MissionResultModel result = routeService.computeMissionResult(empireModel);

        // Verify the expected success probability
        Assertions.assertEquals(testData.expectedSuccessProbability(), result.getMissionSuccessProbability());

        // Verify the expected path
        List<Planet> missionPath = result.getMissionPath();
        List<Planet> expectedPath = testData.expectedPath();
        if (testData.expectedSuccessProbability() == 0) {
            Assertions.assertNull(missionPath);
        } else {
            Assertions.assertEquals(missionPath.size(), expectedPath.size());
            for (int i = 0; i < expectedPath.size(); i++) {
                Assertions.assertEquals(expectedPath.get(i).getName(), missionPath.get(i).getName());
                Assertions.assertEquals(expectedPath.get(i).getDay(), missionPath.get(i).getDay());
                Assertions.assertEquals(expectedPath.get(i).isRefuel(), missionPath.get(i).isRefuel());
            }
        }
    }

}
*/

