package com.dataiku.millenium.entities;

import java.io.Serializable;
import java.util.Objects;

public class RouteId implements Serializable {
    private String origin;
    private String destination;
    private Integer travelTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteId routeId = (RouteId) o;
        return Objects.equals(origin, routeId.origin) && Objects.equals(destination, routeId.destination) && Objects.equals(travelTime, routeId.travelTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, travelTime);
    }
}
