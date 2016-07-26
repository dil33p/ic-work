package com.example.www.transit.model;

/**
 * Created by jaskaran on 23/7/16.
 */
public class Vehicle {
    public String icon;
    public String name;
    public String type;

    public Vehicle(String icon, String type, String name) {
        this.icon = icon;
        this.type = type;
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
