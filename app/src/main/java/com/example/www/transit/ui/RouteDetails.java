package com.example.www.transit.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.www.transit.R;
import com.example.www.transit.model.CustomSteps;
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
    // TODO: complete the activity
    private List<Legs> mLegsList = new ArrayList<Legs>();
    private List<Steps> mStepsList = new ArrayList<Steps>();

    private LinearLayout stepsLayout;
    private TextView stepDistance;
    private TextView stepDuration;
    private TextView stepHtmlInstruction;

    private TextView journeyTime, totalDistance, sourceAddress, departureTime;
    //private TextView transitArrivalStop, transitDepartureStop, transitArrivalTime, transitDepartureTime, vehicleType, vehicleName, numStop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_details);

        journeyTime = (TextView) findViewById(R.id.journey_time);
        totalDistance = (TextView) findViewById(R.id.total_distance);
        sourceAddress = (TextView) findViewById(R.id.from_address);
        departureTime = (TextView) findViewById(R.id.departure_time);

        stepsLayout = (LinearLayout) findViewById(R.id.steps_layout);

        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Routes route = (Routes)getIntent().getSerializableExtra("route");

        mLegsList = route.getLegs();
        for (Legs leg : mLegsList){
            mStepsList = leg.getSteps();
            journeyTime.setText(leg.duration.getText());
            totalDistance.setText(leg.distance.getText());
            sourceAddress.setText(leg.getStartAddress());
            departureTime.setText(leg.DepartureTime.getText());

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

                        /*if (transitArrivalStop.getParent() != null)
                            ((ViewGroup)transitArrivalStop.getParent()).removeView(transitArrivalStop);*/

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

        }
    }
}
