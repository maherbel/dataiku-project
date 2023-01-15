package com.dataiku.millenium.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This class represents the Empire data in the system.
 * <p>
 * It contains fields for storing the countdown, and the bounty hunters positions.
 */
public class EmpireModel {

    private int countdown;

    @JsonProperty("bounty_hunters")
    private List<Planet> bountyHunters;

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public List<Planet> getBountyHunters() {
        return bountyHunters;
    }

    public void setBountyHunters(List<Planet> bountyHunters) {
        this.bountyHunters = bountyHunters;
    }

    @Override
    public String toString() {
        return "EmpireModel{" +
                "countdown=" + countdown +
                ", bountyHunters=" + bountyHunters +
                '}';
    }
}
