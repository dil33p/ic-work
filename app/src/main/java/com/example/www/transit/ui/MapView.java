package com.example.www.transit.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.www.transit.R;
import com.example.www.transit.model.Legs;
import com.example.www.transit.model.Location;
import com.example.www.transit.model.Routes;
import com.example.www.transit.model.Steps;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaskaran on 1/8/16.
 */
public class MapView extends AppCompatActivity implements OnMapReadyCallback {
    private List<Legs> mLegsList = new ArrayList<Legs>();
    GoogleMap mGoogleMap;
    private MarkerOptions origin;
    private MarkerOptions destination;
    private List<Location> polylinePathsTransit = new ArrayList<>();
    private List<Location> polylinePathsWalking = new ArrayList<>();
    private List<LatLng> polyPointsTransit = new ArrayList<>();
    private List<LatLng> polylinePointsWalking = new ArrayList<>();
    private List<Steps> mStepsList = new ArrayList<>();
    private LatLng cameraLatLng;
    PolylineOptions polylineOptionsTransit, polylineOptionsWalking;
    //boolean mReady = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Routes route = (Routes)getIntent().getSerializableExtra("route");

        mLegsList = route.getLegs();
        String mode;
        for (Legs leg : mLegsList){

            final LatLng a = new LatLng(leg.startLocation.lat, leg.startLocation.lng);
            cameraLatLng = a;
            final LatLng b = new LatLng(leg.endLocation.lat, leg.endLocation.lng);
            origin = new MarkerOptions()
                    .position(a)
                    .title(leg.getStartAddress())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp));
            Log.i("StartLocation", "Lat:" + String.valueOf(leg.startLocation.lat) + "Lng: " + String.valueOf(leg.startLocation.lng));
            destination = new MarkerOptions()
                    .position(b)
                    .title(leg.getEndAddress())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp));
            Log.i("End Location", "Lat:" + String.valueOf(leg.endLocation.lat) + "LNG:" + String.valueOf(leg.endLocation.lng));

            mStepsList = leg.getSteps();
            for (Steps step : mStepsList){
                mode = step.getTravelMode();
                polylinePathsTransit = step.getPoints();
                for (Location loc : polylinePathsTransit){
                    final LatLng latLng = new LatLng(loc.lat, loc.lng);
                    polyPointsTransit.add(latLng);
                }
                polylineOptionsTransit = new PolylineOptions()
                        .geodesic(true)
                        .color(getLineColor(mode))
                        .width(15);
                for (int i=0; i<polyPointsTransit.size(); i++){
                    polylineOptionsTransit.add(polyPointsTransit.get(i));
                }

            }
            /*for (Steps step : mStepsList){
                mode = step.getTravelMode();
                if (mode.equals("TRANSIT")){
                    polylinePathsTransit = step.getPoints();
                    for (Location location : polylinePathsTransit){
                        final LatLng latLngPointTransit = new LatLng(location.lat, location.lng);
                        polyPointsTransit.add(latLngPointTransit);
                    }
                    polylineOptionsTransit = new PolylineOptions()
                            .geodesic(true)
                            .color(Color.GREEN)
                            .width(15);
                    for (int i=0; i<polyPointsTransit.size(); i++){
                        polylineOptionsTransit.add(polyPointsTransit.get(i));
                    }
                }

                if (mode.equals("WALKING")){
                    polylinePathsWalking = step.getPoints();
                    for (Location location : polylinePathsWalking){
                        final LatLng latlngPointsWalking = new LatLng(location.lat, location.lng);
                        polylinePointsWalking.add(latlngPointsWalking);
                    }
                    polylineOptionsWalking = new PolylineOptions()
                            .geodesic(true)
                            .color(Color.BLUE)
                            .width(15);
                    for (int i=0; i<polylinePointsWalking.size(); i++){
                        polylineOptionsWalking.add(polylinePointsWalking.get(i));
                    }
                }
            }*/
            /*polylinePaths = Utils.decodePolyLines(route.getPoints());
            for (Location location : polylinePaths){
                final LatLng latLngPoints = new LatLng(location.lat, location.lng);
                polyPoints.add(latLngPoints);
            }
            polylineOptions = new PolylineOptions()
                    .geodesic(true)
                    .color(Color.GREEN)
                    .width(10);
            for (int i=0; i<polyPoints.size(); i++){
                polylineOptions.add(polyPoints.get(i));
            }*/


        }
    }

    private int getLineColor(String mode){
        int color = -1;
        if (mode.equals("TRANSIT")){
            color = Color.CYAN;
        }
        else if (mode.equals("WALKING")){
            color = Color.GREEN;
        }
        return color;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mReady = true;
        mGoogleMap = googleMap;
        mGoogleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        cameraLatLng, 12
                        )
        );
        mGoogleMap.addMarker(origin);
        mGoogleMap.addMarker(destination);
        mGoogleMap.addPolyline(polylineOptionsTransit);
        //mGoogleMap.addPolyline(polylineOptionsWalking);

    }
}
