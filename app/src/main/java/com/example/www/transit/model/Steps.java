package com.example.www.transit.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jaskaran on 20/7/16.
 */
public class Steps implements Serializable{

    public Distance distance;
    public Duration duration;
    public Location endLocation;
    public Location startLocation;
    public String htmlInstructions;
    public String travelMode;
    public List<Location> points;
    public TransitSteps transitSteps;
    public List<CustomSteps> customSteps;

    /*private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeDouble(mLocation.latitude);
        out.writeDouble(mLocation.longitude);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        mLocation = new LatLng(in.readDouble(), in.readDouble());
    }*/


    public List<Location> getPoints() {
        return points;
    }

    public void setPoints(List<Location> points) {
        this.points = points;
    }

    public Distance getDistance() {
        return distance;
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

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public TransitSteps getTransitSteps() {
        return transitSteps;
    }

    public void setTransitSteps(TransitSteps transitSteps) {
        this.transitSteps = transitSteps;
    }

    /*public CustomSteps getCustomSteps() {
        return customSteps;
    }

    public void setCustomSteps(CustomSteps customSteps) {
        this.customSteps = customSteps;
    }*/

    public void setCustomSteps(List<CustomSteps> customSteps) {
        this.customSteps = customSteps;
    }

    public List<CustomSteps> getCustomSteps() {
        return customSteps;
    }

    public void addCustomSteps(CustomSteps customSteps){
        this.customSteps.add(customSteps);
    }
}
