package com.example.www.transit.model;

import java.io.Serializable;

/**
 * Created by jaskaran on 23/7/16.
 */
public class Stops implements Serializable{
    public Location location;
    public String name;

    public Stops(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
