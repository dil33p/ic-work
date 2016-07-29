package com.example.www.transit.model;

import java.io.Serializable;

/**
 * Created by jaskaran on 23/7/16.
 */
public class TransitDetails implements Serializable{

    public Stops arrivalStop;
    public Ctime arrivalTime;
    public Stops departureStop;
    public Ctime departureTime;
    public String headsign;
    public Line line;
    public int numStops;

    public TransitDetails(Stops arrivalStop, Ctime arrivalTime, Stops departureStop, Ctime departureTime, String headsign, Line line, int numStops) {
        this.arrivalStop = arrivalStop;
        this.arrivalTime = arrivalTime;
        this.departureStop = departureStop;
        this.departureTime = departureTime;
        this.headsign = headsign;
        this.line = line;
        this.numStops = numStops;
    }

    public Stops getArrivalStop() {
        return arrivalStop;
    }

    public void setArrivalStop(Stops arrivalStop) {
        this.arrivalStop = arrivalStop;
    }

    public Ctime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Ctime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Stops getDepartureStop() {
        return departureStop;
    }

    public void setDepartureStop(Stops departureStop) {
        this.departureStop = departureStop;
    }

    public Ctime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Ctime departureTime) {
        this.departureTime = departureTime;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public int getNumStops() {
        return numStops;
    }

    public void setNumStops(int numStops) {
        this.numStops = numStops;
    }
}
