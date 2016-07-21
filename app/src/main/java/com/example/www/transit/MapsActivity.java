package com.example.www.transit;

import android.content.Context;
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
import com.example.www.transit.adapters.PlaceAutoCompleteAdapter;
import com.example.www.transit.adapters.RoutesAdapter;
import com.example.www.transit.model.Distance;
import com.example.www.transit.model.Duration;
import com.example.www.transit.model.Legs;
import com.example.www.transit.model.Routes;
import com.example.www.transit.model.Steps;
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
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

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
        mRecyclerAdapter = new RoutesAdapter(mRoutesList, mLegsList);
        mLinearLayoutManager = new LinearLayoutManager(this);

        //url = "https://maps.googleapis.com/maps/api/directions/json?origin=Mathikere,Bengaluru,Karnataka560054&destination=ElectronicCity,Phase1,BusStop,HewlettPackardAvenue,KonappanaAgrahara,ElectronicCity,Bengaluru,Karnataka560100&mode=driving&alternatives=true&key=" + GoogleMapsConstants.API_KEY;

        findRoutes = (Button) findViewById(R.id.find);
        findRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = mSourceAutoText.getText().toString();
                String destination = mDesAutoText.getText().toString();
                String mode = GoogleMapsConstants.MODES.DRIVING;
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
                        for (int j=0; j< legsJSONArray.length(); j++){
                            leg = new Legs();
                            legJSONObject = legsJSONArray.getJSONObject(j);
                            leg.setDistance(new Distance(legJSONObject.optJSONObject("distance").optString("text"), legJSONObject.optJSONObject("distance").optLong("value")));
                            leg.setDuration(new Duration(legJSONObject.optJSONObject("duration").optString("text"), legJSONObject.optJSONObject("duration").optLong("value")));
                            stepsJSONArray = legJSONObject.getJSONArray("steps");
                            JSONObject stepJSONObject, stepDurationJSONObject, legPolyLineJSONObject, stepStartLocationJSONObject, stepEndLocationJSONObject;
                            Steps step;
                            String encodedString;
                            LatLng stepStartLocationLatLng, stepEndLocationLatLng;
                            for (int k=0; k<stepsJSONArray.length(); k++){
                                stepJSONObject = stepsJSONArray.getJSONObject(k);
                                step = new Steps();
                                JSONObject stepDistanceJSONObject = stepJSONObject.getJSONObject("distance");
                                step.setDistance(new Distance(stepDistanceJSONObject.getString("text"), stepDistanceJSONObject.getLong("value")));
                                stepDurationJSONObject = stepJSONObject.getJSONObject("duration");
                                step.setDuration(new Duration(stepDurationJSONObject.getString("text"), stepDurationJSONObject.getLong("value")));
                                stepEndLocationJSONObject = stepJSONObject.getJSONObject("end_location");
                                stepEndLocationLatLng = new LatLng(stepEndLocationJSONObject.getDouble("lat"), stepEndLocationJSONObject.getDouble("lng"));
                                step.setEndLocation(stepEndLocationLatLng);
                                step.setHtmlInstructions(stepJSONObject.getString("html_instructions"));
                                legPolyLineJSONObject = stepJSONObject.getJSONObject("polyline");
                                encodedString = legPolyLineJSONObject.getString("points");
                                step.setPoints(Utils.decodePolyLines(encodedString));
                                stepStartLocationJSONObject = stepJSONObject.getJSONObject("start_location");
                                stepStartLocationLatLng = new LatLng(stepStartLocationJSONObject.getDouble("lat"), stepStartLocationJSONObject.getDouble("lng"));
                                step.setStartLocation(stepStartLocationLatLng);
                                leg.addStep(step);
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

    private String constructUrl(String source, String destination, String mode) throws UnsupportedEncodingException{
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


    // TODO: CHANGE THE MODEL AND PARSE JSON AND SUBMIT BEFORE 3PM. DONT USE MAPS CREATE NEW INTENT AND SHOW ALL SERIALIZED DATA IN NEW INTENT ON BUTTON CLICK.
    // TODO: RECYCLER VIEW SHOULD HAVE TOTAL DISTANCE, SUMMARY AND DURATION; DONT USE LEGS ONLY OVERVIEW DATA.
}
