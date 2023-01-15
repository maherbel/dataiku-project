package com.dataiku.millenium.utils;

import com.dataiku.millenium.pojos.EmpireModel;
import com.dataiku.millenium.pojos.MilleniumFalconModel;
import com.dataiku.millenium.pojos.Planet;

import java.util.List;

public class MockBuilder {
    public EmpireModel buildEmpireModel(int countdown, List<Planet> bountyHuntersPlanets) {
        EmpireModel empireModel = new EmpireModel();
        empireModel.setCountdown(countdown);
        empireModel.setBountyHunters(bountyHuntersPlanets);
        return empireModel;
    }

    public EmpireModel buildEmpireModel(List<Planet> bountyHuntersPlanets) {
        EmpireModel empireModel = new EmpireModel();
        empireModel.setBountyHunters(bountyHuntersPlanets);
        return empireModel;
    }

    public MilleniumFalconModel buildMilleniumFalconModel(int autonomy, String departure, String arrival) {
        MilleniumFalconModel milleniumFalconModel = new MilleniumFalconModel();
        milleniumFalconModel.setAutonomy(autonomy);
        milleniumFalconModel.setDeparture(departure);
        milleniumFalconModel.setArrival(arrival);
        return milleniumFalconModel;
    }
}
