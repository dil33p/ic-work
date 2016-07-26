package com.example.www.transit.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by jaskaran on 26/7/16.
 */
public class CustomSteps extends Steps {
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
    public List<LatLng> getPoints() {
        return super.getPoints();
    }

    @Override
    public void setPoints(List<LatLng> points) {
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
    public LatLng getEndLocation() {
        return super.getEndLocation();
    }

    @Override
    public void setEndLocation(LatLng endLocation) {
        super.setEndLocation(endLocation);
    }

    @Override
    public LatLng getStartLocation() {
        return super.getStartLocation();
    }

    @Override
    public void setStartLocation(LatLng startLocation) {
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
