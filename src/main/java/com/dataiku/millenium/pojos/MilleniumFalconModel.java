package com.dataiku.millenium.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents the Millenium Falcon data in the system.
 * <p>
 * It contains fields for storing the autonomy, departure, arrival and the link to the routes database.
 */
public class MilleniumFalconModel {

    @JsonProperty("autonomy")
    private int autonomy;

    @JsonProperty("departure")
    private String departure;

    @JsonProperty("arrival")
    private String arrival;

    @JsonProperty("routes_db")
    private String routesDB;

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

    public String getRoutesDB() {
        return routesDB;
    }

    public void setRoutesDB(String routesDB) {
        this.routesDB = routesDB;
    }

    @Override
    public String toString() {
        return "AppConfigurationModel{" +
                "autonomy=" + autonomy +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", routesDB='" + routesDB + '\'' +
                '}';
    }
}
