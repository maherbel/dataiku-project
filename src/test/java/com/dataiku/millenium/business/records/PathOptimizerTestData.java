package com.dataiku.millenium.business.records;

import com.dataiku.millenium.entities.Route;
import com.dataiku.millenium.pojos.EmpireModel;
import com.dataiku.millenium.pojos.MilleniumFalconModel;
import com.dataiku.millenium.pojos.Planet;

import java.util.List;

public class PathOptimizerTestData {
    private final EmpireModel empireModel;
    private final List<Route> routes;
    private final MilleniumFalconModel milleniumFalconModel;
    private final double expectedSuccessProbability;

    public PathOptimizerTestData(EmpireModel empireModel, List<Route> routes, MilleniumFalconModel milleniumFalconModel,
                                 double expectedSuccessProbability) {
        this.empireModel = empireModel;
        this.routes = routes;
        this.milleniumFalconModel = milleniumFalconModel;
        this.expectedSuccessProbability = expectedSuccessProbability;
    }

    public EmpireModel getEmpireModel() {
        return empireModel;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public MilleniumFalconModel getMilleniumFalconModel() {
        return milleniumFalconModel;
    }

    public double getExpectedSuccessProbability() {
        return expectedSuccessProbability;
    }
}
