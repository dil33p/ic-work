package com.example.www.transit.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jaskaran on 26/7/16.
 */
public class CustomSteps extends Steps implements Serializable{


    public List<CustomSteps> customStepsList;

    public List<CustomSteps> getCustomStepsList() {
        return customStepsList;
    }

    public void setCustomStepsList(List<CustomSteps> customStepsList) {
        this.customStepsList = customStepsList;
    }

    public void addSteps(CustomSteps cStep){
        this.customStepsList.add(cStep);
    }

    @Override
    public List<Location> getPoints() {
        return super.getPoints();
    }

    @Override
    public void setPoints(List<Location> points) {
        super.setPoints(points);
    }

    @Override
    public Distance getDistance() {
        return super.getDistance();
    }

    @Override
    public void setDistance(Distance distance) {
        super.setDistance(distance);
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }

    @Override
    public Location getEndLocation() {
        return super.getEndLocation();
    }

    @Override
    public void setEndLocation(Location endLocation) {
        super.setEndLocation(endLocation);
    }

    @Override
    public Location getStartLocation() {
        return super.getStartLocation();
    }

    @Override
    public void setStartLocation(Location startLocation) {
        super.setStartLocation(startLocation);
    }

    @Override
    public String getHtmlInstructions() {
        return super.getHtmlInstructions();
    }

    @Override
    public void setHtmlInstructions(String htmlInstructions) {
        super.setHtmlInstructions(htmlInstructions);
    }

    @Override
    public String getTravelMode() {
        return super.getTravelMode();
    }

    @Override
    public void setTravelMode(String travelMode) {
        super.setTravelMode(travelMode);
    }
}
