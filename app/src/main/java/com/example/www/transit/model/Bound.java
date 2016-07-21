package com.example.www.transit.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jaskaran on 21/7/16.
 */
public class Bound {
    private LatLng northEast;
    private LatLng southWest;
    public LatLng getNorthEast() {
        return northEast;
    }
    public void setNorthEast(LatLng northEast) {
        this.northEast = northEast;
    }
    public LatLng getSouthWest() {
        return southWest;
    }
    public void setSouthWest(LatLng southWest) {
        this.southWest = southWest;
    }
}
