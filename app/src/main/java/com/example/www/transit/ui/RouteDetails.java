package com.example.www.transit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.www.transit.DividerItemDecoration;
import com.example.www.transit.R;
import com.example.www.transit.adapters.StepsAdapter;
import com.example.www.transit.model.Legs;
import com.example.www.transit.model.Routes;
import com.example.www.transit.model.Steps;
import com.example.www.transit.model.TransitDetails;
import com.example.www.transit.model.TransitSteps;
import com.example.www.transit.utils.Typefaces;
import com.example.www.transit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaskaran on 27/7/16.
 */
public class RouteDetails extends AppCompatActivity {
    //private List<Routes> mRoutesList = new ArrayList<>();
    private List<Legs> mLegsList = new ArrayList<>();
    private List<Steps> mStepsList = new ArrayList<>();

    /*Recycler Adapter variables*/
    private RecyclerView mRecylerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Context mContext;

    //private List<CustomSteps> customStepsList = new ArrayList<>();

    private TextView journeyDuration;
    private CardView routeCard;

    private LinearLayout parentLayout;
    private TextView title;
    private ImageView mapsView;
    public ImageView vehicleImage, arrowRight, walkImage;
    public TextView busName, walkDistance, arrivalTime, departureTime;
    //private TextView transitArrivalStop, transitDepartureStop, transitArrivalTime, transitDepartureTime, vehicleType, vehicleName, numStop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_details);
        title = (TextView) findViewById(R.id.details);

        int id=0, id1=0;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        routeCard = (CardView) findViewById(R.id.route_card);
        journeyDuration = (TextView) findViewById(R.id.journey_time);
        parentLayout = (LinearLayout) findViewById(R.id.parent);
        mContext = this;
        title.setTypeface(Typefaces.get(mContext, "Roboto-Medium.ttf"));
        mRecylerView = (RecyclerView) findViewById(R.id.steps_list);
        mRecylerView.addItemDecoration(new DividerItemDecoration(
                mContext,
                R.drawable.divider
        ));

        mapsView = (ImageView) findViewById(R.id.map_view);
        int lastView = 0;
        long distance = 0;
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams rp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams rp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams rp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rp.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        rp2.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        final Routes route = (Routes)getIntent().getSerializableExtra("route");

        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(4,4,4,4);

        RelativeLayout lw = new RelativeLayout(this);
        lw.setLayoutParams(lp);
        //lw.setOrientation(LinearLayout.HORIZONTAL);
        lw.setPadding(6,6,6,6);

        parentLayout.addView(ll);
        parentLayout.addView(lw);

        mLegsList = route.getLegs();
        for (Legs leg : mLegsList){
            mStepsList = leg.getSteps();

        }
        mRecyclerAdapter = new StepsAdapter(mStepsList, mContext);
        mRecyclerAdapter.notifyDataSetChanged();

        mLinearLayoutManager = new LinearLayoutManager(this);

        for (Legs leg : mLegsList){
            journeyDuration.setText(leg.duration.getText());
            mStepsList = leg.getSteps();
            for (Steps step : mStepsList){
                id++;
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
                    busName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    busName.setTextColor(getResources().getColor(R.color.primaryText));
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
                    /*for (CustomSteps customStep : customStepsList){
                        if (customStep.getHtmlInstructions() != null)
                        Log.i("Custom Steps", customStep.getDuration().getText());
                    }*/
                }
            }

            walkDistance = new TextView(this);
            walkDistance.setLayoutParams(rp);
            walkDistance.setId(id);
            //walkDistance.setGravity(Gravity.END);
            walkDistance.setText(Utils.convertDistance(distance));

            walkImage = new ImageView(this);
            walkImage.setMaxHeight(50);
            walkImage.setMaxWidth(50);
            rp1.addRule(RelativeLayout.LEFT_OF, walkDistance.getId());
            walkImage.setLayoutParams(rp1);

            Glide.with(this)
                    .load("")
                    .centerCrop()
                    .placeholder(R.drawable.ic_maps_directions_walk)
                    .into(walkImage);

            departureTime = new TextView(this);
            departureTime.setId(id1+1);
            departureTime.setLayoutParams(rp2);
            departureTime.setTypeface(Typefaces.get(mContext, "Roboto-Regular.ttf"));
            departureTime.setTextColor(getResources().getColor(R.color.secondaryText));
            departureTime.setText(leg.DepartureTime.text + " to ");

            arrivalTime = new TextView(this);
            rp3.addRule(RelativeLayout.RIGHT_OF, departureTime.getId());
            arrivalTime.setLayoutParams(rp3);
            arrivalTime.setText(leg.arrivalTime.text);

            lw.addView(departureTime);
            lw.addView(arrivalTime);
            lw.addView(walkImage);
            lw.addView(walkDistance);

            ImageView lastArrow = (ImageView) ll.findViewById(lastView);
            lastArrow.setVisibility(View.GONE);
        }



        setUpRecyclerView(mRecylerView);

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
    }

    private void setUpRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mRecyclerAdapter);
    }
}
