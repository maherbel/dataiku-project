package com.dataiku.millenium.pojos;

import java.util.List;

/**
 * This class represents the result of the Millenium Falcon mission.
 * <p>
 * It contains fields for storing the mission success probability and the path to achieve that probability.
 */
public class MissionResultModel {

    private double missionSuccessProbability;
    private List<Planet> missionPath;
    private List<Planet> riskyPlanets;
    private List<String> errors;

    public double getMissionSuccessProbability() {
        return missionSuccessProbability;
    }

    public void setMissionSuccessProbability(double missionSuccessProbability) {
        this.missionSuccessProbability = missionSuccessProbability;
    }

    public List<Planet> getMissionPath() {
        return missionPath;
    }

    public void setMissionPath(List<Planet> missionPath) {
        this.missionPath = missionPath;
    }

    public List<Planet> getRiskyPlanets() {
        return riskyPlanets;
    }

    public void setRiskyPlanets(List<Planet> riskyPlanets) {
        this.riskyPlanets = riskyPlanets;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
