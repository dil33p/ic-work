package com.example.www.transit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.www.transit.R;
import com.example.www.transit.model.Steps;
import com.example.www.transit.model.TransitDetails;
import com.example.www.transit.model.TransitSteps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaskaran on 5/8/16.
 */
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    private static final String TAG = StepsAdapter.class.getSimpleName();

    //private List<Routes> mRouteList = new ArrayList<>();
    //private List<Legs> mLegsList = new ArrayList<>();
    private List<Steps> mStepsList = new ArrayList<>();
    private Context mContext;

    public StepsAdapter(List<Steps> stepsList, Context context){
        //this.mRouteList = routesList;
        this.mStepsList = stepsList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //final Routes route = mRouteList.get(position);
        //final Legs leg = mLegsList.get(position);
        //mStepsList = leg.getSteps();

        // Check if travel mode of step at 0 is walking then get start time from leg because walking step dont have departure time
        // Steps stepAtzero = mStepsList.get(0);

        //for (final Steps step : mStepsList){
        final Steps step = mStepsList.get(position);
            String mode = step.getTravelMode();
            switch (mode){
                case "WALKING":
                    /*if (stepAtzero.getTravelMode().equals("WALKING")){
                        holder.startTime.setText(leg.DepartureTime.text);
                    }else {
                        holder.startTime.setText();
                        // CANT CREATE AN OBJECT OF TRANSIT DETAILS HERE BECAUSE IT WILL RETURN A NULL POINTER EXCEPTION HERE BEACUSE THE TRANSIT OBJECT WILL BE NULL
                    }*/
                    holder.startTime.setVisibility(View.GONE);
                    Glide.with(mContext)
                            .load("")
                            .placeholder(R.drawable.ic_maps_directions_walk)
                            .into(holder.stepImage);
                    holder.stepDuration.setText(step.duration.getText());
                    holder.stepHtmlInstructions.setText(step.getHtmlInstructions());
                    break;
                case "TRANSIT":
                    TransitSteps transitSteps = step.getTransitSteps();
                    TransitDetails transitDetails = transitSteps.getTransitDetails();
                    String imageUrl = "http:" + transitDetails.line.vehicle.getIcon();

                    holder.stop = new TextView(mContext);
                    holder.stop.setLayoutParams(vp);

                    holder.numStops = new TextView(mContext);
                    holder.numStops.setLayoutParams(vp);

                    holder.stop.setText(transitDetails.departureStop.name);
                    holder.numStops.setText("Number of Stops: " + String.valueOf(transitDetails.numStops));

                    holder.startTime.setText(transitDetails.departureTime.text);
                    Glide.with(mContext)
                            .load(imageUrl)
                            .centerCrop()
                            .override(50,50)
                            .into(holder.stepImage);
                    holder.stepDuration.setText(step.duration.getText());
                    holder.stepHtmlInstructions.setText(step.getHtmlInstructions());

                    holder.stepsLayout.addView(holder.stop);
                    holder.stepsLayout.addView(holder.numStops);
                    break;
            }

        //}
    }

    @Override
    public int getItemCount() {
        return mStepsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView startTime;
        public ImageView stepImage;
        public TextView stepDuration;
        public TextView stepHtmlInstructions;
        public LinearLayout stepsLayout;
        public TextView stop, numStops;
        public ViewHolder(View view){
            super(view);
            startTime = (TextView) view.findViewById(R.id.step_start_time);
            stepImage = (ImageView) view.findViewById(R.id.step_image);
            stepDuration = (TextView) view.findViewById(R.id.step_duration);
            stepHtmlInstructions = (TextView) view.findViewById(R.id.html_instructions);
            stepsLayout = (LinearLayout) view.findViewById(R.id.step_dynamic_layout);
        }
    }
}
