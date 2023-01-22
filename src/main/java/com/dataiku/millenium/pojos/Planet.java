package com.dataiku.millenium.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * This class represents the Planet data in the system.
 * <p>
 * It contains fields for storing the name and the day on which the Millenium Falcon will be stationed.
 */
@JsonIgnoreProperties(value = { "isRefuel", "delay" }, allowGetters=true)
public class Planet {

    @JsonProperty("planet")
    private String name;

    @JsonProperty("day")
    private Integer day;

    private boolean isRefuel;

    private int delay;

    private boolean isRisky;

    public Planet() {

    }

    public Planet(String name) {
        this.name = name;
    }

    public Planet(String name, int day) {
        this.name = name;
        this.day = day;
    }

    public Planet(String name, Integer day, boolean isRefuel) {
        this.name = name;
        this.day = day;
        this.isRefuel = isRefuel;
    }

    public Planet(String name, Integer day, boolean isRefuel, int delay) {
        this.name = name;
        this.day = day;
        this.isRefuel = isRefuel;
        this.delay = delay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public boolean isRefuel() {
        return isRefuel;
    }

    public void setRefuel(boolean refuel) {
        isRefuel = refuel;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isRisky() {
        return isRisky;
    }

    public void setRisky(boolean risky) {
        isRisky = risky;
    }

    public Planet deepCopy() {
        // Create a new object and deep-copy the fields
        return new Planet(this.name, this.day, this.isRefuel, this.delay);
    }

    @Override
    public String toString() {
        return "Planet{" +
                "name='" + name + '\'' +
                ", day='" + day + '\'' +
                (isRefuel ? ", Refuel" : "") +
                (delay > 0 ? ", delay='" + delay + '\'' : "") +
                (isRisky ? ", Risky" : "") +
                '}';
    }
}
