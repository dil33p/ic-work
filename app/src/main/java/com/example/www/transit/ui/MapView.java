package com.example.www.transit.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.www.transit.R;
import com.example.www.transit.model.Legs;
import com.example.www.transit.model.Location;
import com.example.www.transit.model.Routes;
import com.example.www.transit.model.Steps;
import com.example.www.transit.model.TransitDetails;
import com.example.www.transit.model.TransitSteps;
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
    private List<Location> mTransitList = new ArrayList<>();
    private List<LatLng> mTransitLatLngList = new ArrayList<>();
    private List<MarkerOptions> markerOptionsList = new ArrayList<>();
    PolylineOptions polylineOptionsTransit, polylineOptionsWalking;
    //boolean mReady = true;
    private LinearLayout stepLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Routes route = (Routes)getIntent().getSerializableExtra("route");
        String title;
        mLegsList = route.getLegs();
        String mode;
        stepLinearLayout = (LinearLayout) findViewById(R.id.step_image_layout);
        for (Legs leg : mLegsList){

            final LatLng a = new LatLng(leg.startLocation.lat, leg.startLocation.lng);
            cameraLatLng = a;
            final LatLng b = new LatLng(leg.endLocation.lat, leg.endLocation.lng);
            origin = new MarkerOptions()
                    .position(a)
                    .title(leg.getStartAddress())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_room));
            Log.i("StartLocation", "Lat:" + String.valueOf(leg.startLocation.lat) + "Lng: " + String.valueOf(leg.startLocation.lng));
            destination = new MarkerOptions()
                    .position(b)
                    .title(leg.getEndAddress())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_reached));
            Log.i("End Location", "Lat:" + String.valueOf(leg.endLocation.lat) + "LNG:" + String.valueOf(leg.endLocation.lng));
            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

                if (mode.equals("TRANSIT")){
                    TransitSteps transitSteps = step.getTransitSteps();
                    TransitDetails transitDetails = transitSteps.getTransitDetails();
                    mTransitList.add(transitDetails.departureStop.location);
                    String icon = "http:" + transitDetails.line.vehicle.icon;

                    ImageView transitImage = new ImageView(this);
                    transitImage.setMaxHeight(50);
                    transitImage.setMaxWidth(50);
                    transitImage.setLayoutParams(vp);

                    Glide.with(this)
                            .load(icon)
                            .centerCrop()
                            .override(50,50)
                            .into(transitImage);

                    stepLinearLayout.addView(transitImage);

                    // Convert location objects to LatLng objects for adding marker on map.
                    if (transitDetails.line.short_name.equals("null")){
                        title = transitDetails.line.name;
                    }else {
                        title = transitDetails.line.short_name + " " + transitDetails.departureStop.name;
                    }
                    for (Location location : mTransitList){
                        final LatLng latLng = new LatLng(location.lat, location.lng);

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .title(title)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_transit_black_24dp));
                        //mTransitLatLngList.add(latLng);
                        markerOptionsList.add(markerOptions);
                    }
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
        for (MarkerOptions markerOptions : markerOptionsList){
            mGoogleMap.addMarker(markerOptions);
        }

    }
}
