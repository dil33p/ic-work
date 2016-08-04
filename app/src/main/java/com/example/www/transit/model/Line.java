package com.example.www.transit.model;

import java.io.Serializable;

/**
 * Created by jaskaran on 23/7/16.
 */
public class Line implements Serializable{

    public String name;
    public Vehicle vehicle;
    public String short_name;

    public Line(String name, Vehicle vehicle, String short_name) {
        this.name = name;
        this.vehicle = vehicle;
        this.short_name = short_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
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
