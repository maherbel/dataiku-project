package com.dataiku.millenium.business.records;

import com.dataiku.millenium.entities.Route;
import com.dataiku.millenium.pojos.EmpireModel;
import com.dataiku.millenium.pojos.MilleniumFalconModel;
import com.dataiku.millenium.pojos.Planet;

import java.util.List;

public record PathOptimizerTestData(EmpireModel empireModel, List<Route> routes, MilleniumFalconModel milleniumFalconModel,
                                    double expectedSuccessProbability, List<Planet> expectedPath) {
}
