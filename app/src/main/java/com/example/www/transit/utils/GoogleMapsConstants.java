package com.example.www.transit.utils;

/**
 * Created by jaskaran on 19/7/16.
 */
public class GoogleMapsConstants {
    public  static final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    public  static final String API_KEY = "AIzaSyA9WS0-qwTa7P4O-D3Ox5ybcOjGpkcyIV0";
    public static final String DELIMITER = "?";
    public static final String SEPARATOR = "&";
    public static final String RESPONSE_TYPE = "json";




    public interface MODES{
        String DRIVING = "driving";
        String TRANSIT = "transit";
        String WALKING = "walking";
        String CYCLE = "bicycling";
    }

    public  interface TRANSIT{
        String BUS = "bus";
        String TRAIN = "train";
    }

    public interface TrafficModelOptions {
        String BEST_GUESS = "best_guess";
        String PESSIMISTIC = "pessimistic";
        String OPTIMISTIC = "optimistic";
    }

    public interface GoogleDirectionsApiRequestParams {
        String ORIGIN = "origin";              // Compulsory
        String DESTINATION = "destination";    // Compulsory
        String KEY = "key";                    // Compulsory
        String TRANSPORT_MODE = "mode";        // Specifies the mode of transport to use
        String WAY_POINTS = "waypoints";       // Specifies an array of waypoints
        String ALTERNATIVES = "alternatives";  // If true,Provides more than one route alternative in the response
        String AVOID = "avoid";                // Calculated route(s) should avoid the indicated features
        String LANGUAGE = "language";          // Specifies the language in which to return results
        String UNITS = "units";                // Specifies the unit system to use when displaying results
        String REGION = "region";
        String ARRIVAL_TIME = "arrival_time";     // Specifies the desired time of arrival for transit directions, in seconds since midnight, January 1, 1970 UTC
        String DEPARTURE_TIME = "departure_time"; // Specifies the desired time of departure
        String TRAFFIC_MODEL = "traffic_model";   // Specifies the assumptions to use when calculating time in traffic
        String TRANSIT_MODE = "transit_mode";
        String TRANSIT_ROUTING_PREFERENCE = "transit_routing_preference";
    }
}
