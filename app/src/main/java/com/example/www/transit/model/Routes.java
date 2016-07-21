package com.example.www.transit.model;

import android.content.Context;

import com.google.android.gms.maps.model.Polyline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaskaran on 20/7/16.
 */
public class Routes implements Serializable{

    public Bound bounds;
    public String copyrights;
    public List<Legs> legs;
    public Polyline overviewPolyLine;
    public String summary;

    public Routes(Context context) {
        legs = new ArrayList<Legs>();
    }

    public Bound getBounds() {
        return bounds;
    }

    public void setBounds(Bound bounds) {
        this.bounds = bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public List<Legs> getLegs() {
        return legs;
    }

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }

    public void addLeg(Legs leg) {
        this.legs.add(leg);
    }

    public Polyline getOverviewPolyLine() {
        return overviewPolyLine;
    }

    public void setOverviewPolyLine(Polyline overviewPolyLine) {
        this.overviewPolyLine = overviewPolyLine;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
