package com.example.www.transit.model;

/**
 * Created by jaskaran on 23/7/16.
 */
public class Line {

    public String name;
    public Vehicle vehicle;

    public Line(String name, Vehicle vehicle) {
        this.name = name;
        this.vehicle = vehicle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
