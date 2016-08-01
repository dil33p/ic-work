package com.example.www.transit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaskaran on 20/7/16.
 */
public class Legs implements Serializable{

    public Distance distance;
    public Duration duration;
    public String endAddress;
    public Location endLocation;
    public String startAddress;
    public Location startLocation;
    public List<Steps> steps;
    public Ctime arrivalTime;
    public Ctime DepartureTime;

    public Legs() {
        steps = new ArrayList<Steps>();
    }

    public Distance getDistance() {
        return distance;
    }

    public Ctime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Ctime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Ctime getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime(Ctime departureTime) {
        DepartureTime = departureTime;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public void addStep(Steps step) {
        this.steps.add(step);
    }

}

