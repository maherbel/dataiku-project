package com.dataiku.millenium.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * The `Route` class represents a route between two planets in the galaxy.
 * It consists of an origin planet, a destination planet, and a travel time.
 * It is used to store the data of routes between planets, and is
 * used in the PathOptimizer class to compute the optimal path for a given mission.
 */
@Entity
@Table(name = "routes")
@IdClass(RouteId.class)
public class Route {

    /**
     * The origin planet of the route
     */
    @Id
    @Column(name = "origin", nullable = false)
    private String origin;

    /**
     * The destination planet of the route
     */
    @Id
    @Column(name = "destination", nullable = false)
    private String destination;

    /**
     * The travel time of the route
     */
    @Id
    @Column(name = "travel_time", nullable = false)
    private Integer travelTime;

    /**
     * Default constructor
     */
    public Route() {

    }

    /**
     * Constructor with parameters
     *
     * @param origin      the origin planet
     * @param destination the destination planet
     * @param travelTime  the travel time
     */
    public Route(String origin, String destination, Integer travelTime) {
        this.origin = origin;
        this.destination = destination;
        this.travelTime = travelTime;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, travelTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(origin, route.origin) &&
                Objects.equals(destination, route.destination) &&
                Objects.equals(travelTime, route.travelTime);
    }

    @Override
    public String toString() {
        return "Route{" +
                "origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", travelTime=" + travelTime +
                '}';
    }
}
