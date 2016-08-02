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
import com.example.www.transit.utils.Utils;
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
    private List<Location> polylinePaths = new ArrayList<>();
    private List<LatLng> polyPoints = new ArrayList<>();
    private LatLng cameraLatLng;
    PolylineOptions polylineOptions;
    //boolean mReady = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Routes route = (Routes)getIntent().getSerializableExtra("route");

        mLegsList = route.getLegs();
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

            polylinePaths = Utils.decodePolyLines(route.getPoints());
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
            }
        }
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
        mGoogleMap.addPolyline(polylineOptions);

    }
}
