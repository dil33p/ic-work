package com.example.www.transit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.www.transit.R;
import com.example.www.transit.adapters.PlaceAutoCompleteAdapter;
import com.example.www.transit.adapters.RoutesAdapter;
import com.example.www.transit.model.Ctime;
import com.example.www.transit.model.CustomSteps;
import com.example.www.transit.model.Distance;
import com.example.www.transit.model.Duration;
import com.example.www.transit.model.Legs;
import com.example.www.transit.model.Line;
import com.example.www.transit.model.Location;
import com.example.www.transit.model.Routes;
import com.example.www.transit.model.Steps;
import com.example.www.transit.model.Stops;
import com.example.www.transit.model.TransitDetails;
import com.example.www.transit.model.TransitSteps;
import com.example.www.transit.model.Vehicle;
import com.example.www.transit.utils.GoogleMapsConstants;
import com.example.www.transit.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, RoutesAdapter.OnAdapterItemSelectedListener{

    private Button findRoutes;

    private static final String TAG = "Maps";

    //private GoogleMap mMap;

    protected GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView mSourceAutoText, mDesAutoText;

    private PlaceAutoCompleteAdapter mAdapter;

    private RecyclerView mRecylcerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private List<Routes> mRoutesList = new ArrayList<Routes>();
    private List<Legs> mLegsList = new ArrayList<Legs>();
    //private List<Steps> mStepsList = new ArrayList<Steps>();

    private RequestQueue mRequestQueue;

    private String url;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        buildGoogleApiClient();
        context = this;
        mRequestQueue = Volley.newRequestQueue(this);

        mRecylcerView = (RecyclerView) findViewById(R.id.card_list);
        mRecyclerAdapter = new RoutesAdapter(mRoutesList, mLegsList, context, this);
        mLinearLayoutManager = new LinearLayoutManager(this);

        //url = "https://maps.googleapis.com/maps/api/directions/json?origin=Mathikere,Bengaluru,Karnataka560054&destination=ElectronicCity,Phase1,BusStop,HewlettPackardAvenue,KonappanaAgrahara,ElectronicCity,Bengaluru,Karnataka560100&mode=driving&alternatives=true&key=" + GoogleMapsConstants.API_KEY;

        findRoutes = (Button) findViewById(R.id.find);
        findRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = mSourceAutoText.getText().toString();
                String destination = mDesAutoText.getText().toString();
                String mode = GoogleMapsConstants.MODES.TRANSIT;
                if (source.isEmpty()){
                    Toast.makeText(context, "Enter Source destination", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (destination.isEmpty()){
                    Toast.makeText(context, "Enter End Destination", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    url = constructUrl(source, destination, mode);
                    Log.d("URL: ", url);
                    //getData(url);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("Error in url", e.toString());
                }
                mRoutesList.clear();
                mLegsList.clear();
                //mStepsList.clear();
                getData(url);
                setUpRecyclerView(mRecylcerView);
            }
        });

        mSourceAutoText = (AutoCompleteTextView) findViewById(R.id.auto_to_loc);
        mDesAutoText = (AutoCompleteTextView) findViewById(R.id.auto_to_des);

        mAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient, null, null);
        mSourceAutoText.setAdapter(mAdapter);
        mDesAutoText.setAdapter(mAdapter);

        /*// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this)*/;

        mSourceAutoText.setOnItemClickListener(mAutocompleteClickListener);
        mDesAutoText.setOnItemClickListener(mAutocompleteClickListener);

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /*
                     Retrieve the Place ID of the selected item from the Adapter.
                     The adapter stores each Place suggestion in AutoCompletePrediction from
                     which we read place ID and title.
                     */
                    final AutocompletePrediction item = mAdapter.getItem(position);
                    final String placeId = item.getPlaceId();
                    final CharSequence primaryText = item.getPrimaryText(null);

                    Log.i(TAG, "Autocomplete item selected: " + primaryText);

                    /*
                     Issue a request to the Places Geo Data API to retrieve a Place object with additional
                     details about the place.
                     */
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                            .getPlaceById(mGoogleApiClient, placeId);
                    placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

                }
            };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);
            Log.i(TAG, "Source: " + mSourceAutoText.getText());
            Log.i(TAG, "Dest: " + mDesAutoText.getText());
        }
    };


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //getData(url);
    }*/

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Failed to connect to Google Api Client");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private List<Routes> getData(String url){
        JsonObjectRequest getListData = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray routesArray = response.getJSONArray("routes");
                    JSONObject routesJSONObject;
                    for (int i = 0; i < routesArray.length(); i++) {
                        Routes route = new Routes(context);
                        routesJSONObject = routesArray.getJSONObject(i);
                        JSONArray legsJSONArray;
                        route.setSummary(routesJSONObject.getString("summary"));
                        legsJSONArray = routesJSONObject.getJSONArray("legs");
                        JSONObject legJSONObject;
                        Legs leg;
                        JSONArray stepsJSONArray;

                        List<CustomSteps> customStepsList = new ArrayList<CustomSteps>();
                        for (int j=0; j< legsJSONArray.length(); j++){
                            leg = new Legs();
                            legJSONObject = legsJSONArray.getJSONObject(j);
                            leg.setDistance(new Distance(legJSONObject.optJSONObject("distance").optString("text"), legJSONObject.optJSONObject("distance").optLong("value")));
                            leg.setDuration(new Duration(legJSONObject.optJSONObject("duration").optString("text"), legJSONObject.optJSONObject("duration").optLong("value")));
                            leg.setArrivalTime(new Ctime(legJSONObject.optJSONObject("arrival_time").optString("text"), legJSONObject.optJSONObject("arrival_time").optString("time_zone"), legJSONObject.optJSONObject("arrival_time").optLong("value")));
                            leg.setDepartureTime(new Ctime(legJSONObject.optJSONObject("departure_time").optString("text"), legJSONObject.optJSONObject("departure_time").optString("time_zone"), legJSONObject.optJSONObject("departure_time").optLong("value")));
                            leg.setStartAddress(legJSONObject.getString("start_address"));
                            leg.setEndAddress(legJSONObject.getString("end_address"));

                            stepsJSONArray = legJSONObject.getJSONArray("steps");
                            JSONObject stepJSONObject, stepDurationJSONObject, legPolyLineJSONObject, stepStartLocationJSONObject, stepEndLocationJSONObject, stepTransitDetails;

                            JSONObject transitArrivalStopJsonObject, transitDepartureStopJsonObject;
                            JSONObject arrivalStopLocJsonObject, departureStopLocJsonObject;

                            Location arrivalStopLoc, departureStopLoc;

                            Stops arrivalStop, departureStop;
                            TransitDetails transitDetail;

                            JSONObject arrivalTimeJsonObject, departureTimeJsonObject;

                            Ctime arrivalTime, departureTime;
                            String headsign = null;
                            String stepTravelMode = null;

                            JSONObject lineJsonObject, vehicleJsonObject;
                            Line line;
                            Vehicle vehicle;
                            int numStops;

                            Steps step;
                            TransitSteps tStep;
                            String encodedString;
                            Location stepStartLocationLatLng, stepEndLocationLatLng;

                            CustomSteps cStep;
                            JSONArray cStepJsonArray;
                            JSONObject cStepJsonObject;

                            for (int k=0; k<stepsJSONArray.length(); k++){

                                stepJSONObject = stepsJSONArray.getJSONObject(k);
                                step = new Steps();
                                JSONObject stepDistanceJSONObject = stepJSONObject.getJSONObject("distance");
                                step.setDistance(new Distance(stepDistanceJSONObject.getString("text"), stepDistanceJSONObject.getLong("value")));
                                stepDurationJSONObject = stepJSONObject.getJSONObject("duration");
                                step.setDuration(new Duration(stepDurationJSONObject.getString("text"), stepDurationJSONObject.getLong("value")));
                                stepEndLocationJSONObject = stepJSONObject.getJSONObject("end_location");
                                stepEndLocationLatLng = new Location(stepEndLocationJSONObject.getDouble("lat"), stepEndLocationJSONObject.getDouble("lng"));
                                step.setEndLocation(stepEndLocationLatLng);
                                step.setHtmlInstructions(stepJSONObject.getString("html_instructions"));
                                legPolyLineJSONObject = stepJSONObject.getJSONObject("polyline");
                                encodedString = legPolyLineJSONObject.getString("points");
                                step.setPoints(Utils.decodePolyLines(encodedString));
                                stepStartLocationJSONObject = stepJSONObject.getJSONObject("start_location");
                                stepStartLocationLatLng = new Location(stepStartLocationJSONObject.getDouble("lat"), stepStartLocationJSONObject.getDouble("lng"));
                                step.setStartLocation(stepStartLocationLatLng);
                                stepTravelMode = stepJSONObject.getString("travel_mode");
                                step.setTravelMode(stepTravelMode);

                                if (step.getTravelMode().equals("TRANSIT")){
                                    tStep = new TransitSteps();
                                    stepTransitDetails = stepJSONObject.getJSONObject("transit_details");

                                    transitArrivalStopJsonObject = stepTransitDetails.getJSONObject("arrival_stop");
                                    arrivalStopLocJsonObject = transitArrivalStopJsonObject.getJSONObject("location");
                                    arrivalStopLoc = new Location(arrivalStopLocJsonObject.getDouble("lat"), arrivalStopLocJsonObject.getDouble("lng"));

                                    arrivalStop = new Stops(arrivalStopLoc, transitArrivalStopJsonObject.getString("name"));

                                    arrivalTimeJsonObject = stepTransitDetails.getJSONObject("arrival_time");
                                    arrivalTime = new Ctime(arrivalTimeJsonObject.getString("text"), arrivalTimeJsonObject.getString("time_zone"), arrivalTimeJsonObject.getLong("value"));

                                    transitDepartureStopJsonObject = stepTransitDetails.getJSONObject("departure_stop");
                                    departureStopLocJsonObject = transitDepartureStopJsonObject.getJSONObject("location");
                                    departureStopLoc = new Location(departureStopLocJsonObject.getDouble("lat"), departureStopLocJsonObject.getDouble("lng"));

                                    departureStop = new Stops(departureStopLoc, transitDepartureStopJsonObject.getString("name"));

                                    departureTimeJsonObject = stepTransitDetails.getJSONObject("departure_time");
                                    departureTime = new Ctime(departureTimeJsonObject.getString("text"), departureTimeJsonObject.getString("time_zone"), departureTimeJsonObject.getLong("value"));

                                    headsign = stepTransitDetails.getString("headsign");

                                    lineJsonObject = stepTransitDetails.getJSONObject("line");
                                    vehicleJsonObject = lineJsonObject.getJSONObject("vehicle");
                                    vehicle = new Vehicle(vehicleJsonObject.getString("icon"), vehicleJsonObject.getString("name"), vehicleJsonObject.getString("type"));

                                    line = new Line(lineJsonObject.getString("name"), vehicle);
                                    numStops = stepTransitDetails.getInt("num_stops");
                                    transitDetail = new TransitDetails(arrivalStop, arrivalTime, departureStop, departureTime, headsign, line, numStops);
                                    tStep.setTransitDetails(transitDetail);
                                    step.setTransitSteps(tStep);
                                }

                                else if (step.getTravelMode().equals("WALKING")){
                                    cStep = new CustomSteps();
                                    cStepJsonArray = stepJSONObject.getJSONArray("steps");
                                    for (int x=0; x<cStepJsonArray.length(); x++){
                                        cStepJsonObject = cStepJsonArray.getJSONObject(x);

                                        JSONObject cDistanceJsonObject = cStepJsonObject.getJSONObject("distance");
                                        Distance cDistance = new Distance(cDistanceJsonObject.getString("text"), cDistanceJsonObject.getLong("value"));

                                        JSONObject cDurationJsonObject = cStepJsonObject.getJSONObject("duration");
                                        Duration cDuration = new Duration(cDurationJsonObject.getString("text"), cDurationJsonObject.getLong("value"));

                                        JSONObject cEndLocationJsonObject = cStepJsonObject.getJSONObject("end_location");
                                        Location cEndLocation = new Location(cEndLocationJsonObject.getLong("lat"), cEndLocationJsonObject.getLong("lng"));



                                        JSONObject cPolylinesJsonObject = cStepJsonObject.getJSONObject("polyline");
                                        String codedString = cPolylinesJsonObject.getString("points");

                                        JSONObject cStartLocationJsonObject = cStepJsonObject.getJSONObject("start_location");
                                        Location cStartLocation = new Location(cStartLocationJsonObject.getLong("lat"), cStartLocationJsonObject.getLong("lng"));

                                        cStep.setDistance(cDistance);
                                        cStep.setDuration(cDuration);
                                        cStep.setEndLocation(cEndLocation);
                                        //cStep.setHtmlInstructions(htmlInstructions);
                                        cStep.setPoints(Utils.decodePolyLines(codedString));
                                        cStep.setStartLocation(cStartLocation);
                                        cStep.setTravelMode(cStepJsonObject.getString("travel_mode"));

                                        if (cStepJsonObject.has("html_instructions")) {
                                            String htmlInstructions = cStepJsonObject.getString("html_instructions");
                                            cStep.setHtmlInstructions(htmlInstructions);
                                        }

                                        customStepsList.add(cStep);
                                        //step.addCustomSteps(cStep);
                                    }
                                    step.setCustomSteps(customStepsList);
                                }
                                leg.addStep(step);
                                //mStepsList.add(step);
                            }
                            route.addLeg(leg);
                            mLegsList.add(leg);
                            Log.i("LEGS DISTANCE", String.valueOf(leg.distance.getValue()));
                            Log.i("LEGS DURATION", String.valueOf(leg.duration.getValue()));
                        }
                        mRoutesList.add(route);
                        Log.i("TOTAL ROUTES", String.valueOf(mRoutesList.size()));
                        Log.i("SUMMARY: ", route.getSummary());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRecyclerAdapter.notifyDataSetChanged();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.toString());
                    }
                });
        mRequestQueue.add(getListData);
        return mRoutesList;
    }

    private void setUpRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mRecyclerAdapter);
    }

    private String constructUrl(String source, String destination, String mode) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append(GoogleMapsConstants.BASE_URL);
        builder.append(GoogleMapsConstants.RESPONSE_TYPE);

        getParamAppendBuider(GoogleMapsConstants.GoogleDirectionsApiRequestParams.ORIGIN, URLEncoder.encode(source, "utf-8"), builder);
        getParamAppendBuider(GoogleMapsConstants.GoogleDirectionsApiRequestParams.DESTINATION, URLEncoder.encode(destination, "utf-8"), builder);
        getParamAppendBuider(GoogleMapsConstants.GoogleDirectionsApiRequestParams.TRANSPORT_MODE, mode, builder);
        getParamAppendBuider(GoogleMapsConstants.GoogleDirectionsApiRequestParams.ALTERNATIVES, "true", builder);
        getParamAppendBuider(GoogleMapsConstants.GoogleDirectionsApiRequestParams.KEY, GoogleMapsConstants.API_KEY, builder);
        return builder.toString();
    }

    private StringBuilder getParamAppendBuider(String paramName, String paramValue, StringBuilder builder){
        if (builder.toString().contains(GoogleMapsConstants.DELIMITER)){
            builder.append(GoogleMapsConstants.SEPARATOR);
        }else {
            builder.append(GoogleMapsConstants.DELIMITER);
        }
        builder.append(paramName);
        builder.append("=");
        builder.append(paramValue);
        return builder;
    }

    @Override
    public void onItemSelected(Routes route) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("route", route);
        Intent routeDetailsActivity = new Intent(this, RouteDetails.class)
                .putExtras(bundle);
        startActivity(routeDetailsActivity);
    }

}
