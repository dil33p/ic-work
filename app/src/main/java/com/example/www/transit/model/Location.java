package com.example.www.transit.model;

/**
 * Created by jaskaran on 20/7/16.
 */
public class Location {

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double lat;
    public double lng;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
