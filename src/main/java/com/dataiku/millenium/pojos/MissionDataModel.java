package com.dataiku.millenium.pojos;

import java.util.Map;

/**
 * This class represents the Mission data in the system.
 * <p>
 * It contains fields for storing the autonomy, departure, arrival and the different planets with their
 * travel times to each of their neighbors.
 */
public class MissionDataModel {

    private int autonomy;
    private String departure;
    private String arrival;
    private Map<Planet, Map<Planet, Integer>> nodes;

    public int getAutonomy() {
        return autonomy;
    }

    public void setAutonomy(int autonomy) {
        this.autonomy = autonomy;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public Map<Planet, Map<Planet, Integer>> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Planet, Map<Planet, Integer>> nodes) {
        this.nodes = nodes;
    }
}
