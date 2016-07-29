package com.example.www.transit.model;

import java.io.Serializable;

/**
 * Created by jaskaran on 26/7/16.
 */
public class TransitSteps extends Steps implements Serializable{
    public TransitDetails transitDetails;

    public TransitDetails getTransitDetails() {
        return transitDetails;
    }

    public void setTransitDetails(TransitDetails transitDetails) {
        this.transitDetails = transitDetails;
    }
}
