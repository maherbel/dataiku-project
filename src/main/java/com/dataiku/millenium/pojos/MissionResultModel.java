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
}
