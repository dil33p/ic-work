package com.example.www.transit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.www.transit.R;
import com.example.www.transit.model.Legs;
import com.example.www.transit.model.Routes;
import com.example.www.transit.model.Steps;
import com.example.www.transit.model.TransitDetails;
import com.example.www.transit.model.TransitSteps;
import com.example.www.transit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaskaran on 27/7/16.
 */
public class RouteDetails extends AppCompatActivity {
    private List<Legs> mLegsList = new ArrayList<>();
    private List<Steps> mStepsList = new ArrayList<>();

    private LinearLayout stepsLayout;
    private TextView stepDistance;
    private TextView stepDuration;
    private TextView stepHtmlInstruction;

    private TextView journeyDuration;
    private CardView routeCard;

    private LinearLayout parentLayout;

    private ImageView mapsView;
    public ImageView vehicleImage, arrowRight, walkImage;
    public TextView busName, walkDistance, arrivalTime, departureTime;
    //private TextView transitArrivalStop, transitDepartureStop, transitArrivalTime, transitDepartureTime, vehicleType, vehicleName, numStop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_details);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        routeCard = (CardView) findViewById(R.id.route_card);
        journeyDuration = (TextView) findViewById(R.id.journey_time);
        parentLayout = (LinearLayout) findViewById(R.id.parent);

        /*totalDistance = (TextView) findViewById(R.id.total_distance);
        sourceAddress = (TextView) findViewById(R.id.from_address);
        departureTime = (TextView) findViewById(R.id.departure_time);*/

        //stepsLayout = (LinearLayout) findViewById(R.id.steps_layout);
        mapsView = (ImageView) findViewById(R.id.map_view);
        int lastView = 0;
        long distance = 0;
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final Routes route = (Routes)getIntent().getSerializableExtra("route");

        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(4,4,4,4);

        LinearLayout lw = new LinearLayout(this);
        lw.setLayoutParams(lp);
        lw.setOrientation(LinearLayout.HORIZONTAL);
        lw.setPadding(4,4,4,4);

        parentLayout.addView(ll);
        parentLayout.addView(lw);

        mLegsList = route.getLegs();
        for (Legs leg : mLegsList){
            journeyDuration.setText(leg.duration.getText());
            mStepsList = leg.getSteps();
            for (Steps step : mStepsList){
                String mode = step.getTravelMode();
                if (mode.equals("TRANSIT")){
                    lastView++;
                    TransitSteps transitSteps = step.getTransitSteps();
                    TransitDetails transitDetails = transitSteps.getTransitDetails();
                    vehicleImage = new ImageView(this);
                    arrowRight = new ImageView(this);
                    arrowRight.setLayoutParams(vp);
                    arrowRight.setMaxHeight(50);
                    arrowRight.setMaxWidth(50);
                    arrowRight.setId(lastView);
                    busName = new TextView(this);
                    busName.setLayoutParams(vp);
                    vehicleImage.setLayoutParams(vp);
                    vehicleImage.setMaxHeight(50);
                    vehicleImage.setMaxWidth(50);
                    Glide.with(this)
                            .load("http:"+transitDetails.line.vehicle.icon)
                            .centerCrop()
                            .override(50,50)
                            .into(vehicleImage);
                    String name = transitDetails.line.name;
                    String shortName = transitDetails.line.short_name;
                    if (shortName.equals("null")){
                        busName.setText(name);
                    }else {
                        busName.setText(shortName);
                    }
                    Glide.with(this)
                            .load("")
                            .placeholder(R.drawable.ic_hardware_keyboard_arrow_right)
                            .centerCrop()
                            .into(arrowRight);
                    ll.addView(vehicleImage);
                    ll.addView(busName);
                    ll.addView(arrowRight);
                }
                if (mode.equals("WALKING")){
                    distance += step.distance.value;

                }
            }

            walkDistance = new TextView(this);
            walkDistance.setLayoutParams(vp);
            walkDistance.setGravity(Gravity.END);

            walkDistance.setText(Utils.convertDistance(distance));

            walkImage = new ImageView(this);
            walkImage.setMaxHeight(50);
            walkImage.setMaxWidth(50);
            walkImage.setLayoutParams(vp);

            Glide.with(this)
                    .load("")
                    .centerCrop()
                    .placeholder(R.drawable.ic_maps_directions_walk)
                    .into(walkImage);

            departureTime = new TextView(this);
            departureTime.setLayoutParams(vp);
            departureTime.setText(leg.DepartureTime.text + " to ");

            arrivalTime = new TextView(this);
            arrivalTime.setLayoutParams(vp);
            arrivalTime.setText(leg.arrivalTime.text);

            lw.addView(departureTime);
            lw.addView(arrivalTime);
            lw.addView(walkImage);
            lw.addView(walkDistance);

            ImageView lastArrow = (ImageView) ll.findViewById(lastView);
            lastArrow.setVisibility(View.GONE);
        }

        mapsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("route", route);
                Intent intent = new Intent(RouteDetails.this, MapView.class)
                        .putExtras(bundle);
                startActivity(intent);

            }
        });
        /*for (Legs leg : mLegsList){
            mStepsList = leg.getSteps();
            journeyTime.setText(leg.duration.getText());
            *//*totalDistance.setText(leg.distance.getText());
            sourceAddress.setText(leg.getStartAddress());
            departureTime.setText(leg.DepartureTime.getText());*//*

            for (Steps step : mStepsList){
                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setPadding(4, 4, 4, 4);

                stepHtmlInstruction = new TextView(this);
                stepDistance = new TextView(this);
                stepDuration = new TextView(this);


                stepDistance.setLayoutParams(vp);
                stepDuration.setLayoutParams(vp);
                stepHtmlInstruction.setLayoutParams(vp);

                switch (step.getTravelMode()){
                    case "TRANSIT":

                        TransitSteps transitStep = step.getTransitSteps();
                        TransitDetails transitDetails = transitStep.getTransitDetails();
                        int numStpos = transitDetails.getNumStops();
                        Log.i("Custom Steps", String.valueOf(numStpos));
                        TextView transitArrivalStop = new TextView(this);
                        TextView transitArrivalTime = new TextView(this);
                        TextView transitDepartureStop = new TextView(this);
                        TextView transitDepartureTime = new TextView(this);
                        TextView vehicleName = new TextView(this);
                        TextView vehicleType = new TextView(this);
                        TextView numStop = new TextView(this);

                        transitArrivalStop.setLayoutParams(vp);
                        transitDepartureStop.setLayoutParams(vp);
                        transitArrivalTime.setLayoutParams(vp);
                        transitDepartureTime.setLayoutParams(vp);
                        vehicleName.setLayoutParams(vp);
                        vehicleType.setLayoutParams(vp);
                        numStop.setLayoutParams(vp);

                        transitArrivalStop.setText("Arrival Stop " + transitDetails.arrivalStop.getName());
                        transitArrivalTime.setText("Arrival Time " + transitDetails.arrivalTime.getText());
                        transitDepartureTime.setText("Departure Time " + transitDetails.departureTime.getText());
                        transitDepartureStop.setText("Departure Stop " + transitDetails.departureStop.getName());
                        vehicleType.setText("Type " + transitDetails.line.vehicle.getType());
                        vehicleName.setText("Name " + transitDetails.line.getName());
                        numStop.setText("Stops " + String.valueOf(numStpos));

                        *//*if (transitArrivalStop.getParent() != null)
                            ((ViewGroup)transitArrivalStop.getParent()).removeView(transitArrivalStop);*//*

                        ll.addView(transitArrivalStop);
                        ll.addView(transitArrivalTime);
                        ll.addView(transitDepartureStop);
                        ll.addView(transitDepartureTime);
                        ll.addView(vehicleType);
                        ll.addView(vehicleName);
                        ll.addView(numStop);
                        break;

                    case "WALKING":
                        List<CustomSteps> customSteps = step.getCustomSteps();
                        for (CustomSteps cStep : customSteps){
                            if (cStep.htmlInstructions != null) {
                                Log.i("Custom Steps", Utils.decodeInstructions(cStep.getHtmlInstructions()));
                            }
                            Log.i("Custom Steps", cStep.duration.getText());
                            Log.i("Custom Steps", cStep.distance.getText());
                        }
                }

                stepHtmlInstruction.setText(Utils.decodeInstructions(step.getHtmlInstructions()));
                stepDistance.setText(step.distance.getText());
                stepDuration.setText(step.duration.getText());

                ll.addView(stepHtmlInstruction);
                ll.addView(stepDistance);
                ll.addView(stepDuration);

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                imageView.setMaxHeight(50);
                imageView.setMaxWidth(50);
                Glide.with(this)
                        .load("")
                        .placeholder(R.drawable.ic_more_vert_black_24dp)
                        .centerCrop()
                        .into(imageView);
                ll.addView(imageView);
                stepsLayout.addView(ll);
            }

        }*/
    }
}
