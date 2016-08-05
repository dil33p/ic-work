package com.example.www.transit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by jaskaran on 21/7/16.
 */
public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {
    private static final String TAG = RoutesAdapter.class.getSimpleName();

    private List<Routes> mRoutesList = new ArrayList<>();
    private List<Legs> mLegsList = new ArrayList<>();
    Context mContext;
    private OnAdapterItemSelectedListener mAdapterCallback;

    //private List<Steps> mStepsList = new ArrayList<>();
    public RoutesAdapter(List<Routes> routesList, List<Legs> legsList, Context context, OnAdapterItemSelectedListener adapterItemSelectedListener){
        this.mRoutesList = routesList;
        this.mLegsList = legsList;
        this.mContext = context;
        this.mAdapterCallback = (adapterItemSelectedListener);
        //this.mStepsList = stepsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(itemView);
    }

    public interface OnAdapterItemSelectedListener{
        void onItemSelected(Routes routes);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Routes route = mRoutesList.get(position);
        final Legs leg = mLegsList.get(position);
        holder.duration.setText(leg.duration.getText());
        List<Steps> steps = leg.getSteps();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapterCallback != null){
                    mAdapterCallback.onItemSelected(route);
                }
            }
        });


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(4,4,4,4);
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        holder.linearLayout.addView(ll);
        String mode = "";

        LinearLayout lw = new LinearLayout(mContext);
        lw.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(6,6,6,6);


        holder.linearLayout.addView(lw);

        int lastView = 0;
        long distance = 0;
        for (final Steps step : steps){

            Log.i(TAG, step.getTravelMode().toString() + position);
            /* mode += step.getTravelMode().toString() + " " + " > " + " " ;
            holder.travelMode.setText(mode);*/
            mode = step.getTravelMode().toString();

            if (mode.equals("TRANSIT")){
                lastView++;
                TransitSteps transitSteps = step.getTransitSteps();
                TransitDetails transitDetails = transitSteps.getTransitDetails();
                holder.imageView = new ImageView(mContext);

                holder.imageView.setLayoutParams(vp);
                holder.imageView.setMaxHeight(50);
                holder.imageView.setMaxWidth(50);

                holder.busName = new TextView(mContext);
                holder.busName.setLayoutParams(vp);
                String shortName = transitDetails.line.short_name;
                String name = transitDetails.line.name;
                if (shortName.equals("null")){
                    holder.busName.setText(name);
                }else {
                    holder.busName.setText(shortName);
                }

                holder.arrows = new ImageView(mContext);
                holder.arrows.setId(lastView);
                holder.arrows.setLayoutParams(vp);
                holder.arrows.setMaxWidth(50);
                holder.arrows.setMaxHeight(50);

                String a = transitDetails.line.vehicle.icon;
                String b = a.substring(2);
                String c = "http://"+b;
                System.out.println(b);

                Glide.with(mContext)
                        .load(c)
                        .fitCenter()
                        .centerCrop()
                        .override(50,50)
                        .into(holder.imageView);

                Glide.with(mContext)
                        .load("")
                        .placeholder(R.drawable.ic_hardware_keyboard_arrow_right)
                        .into(holder.arrows);

                ll.addView(holder.imageView);
                ll.addView(holder.busName);
                ll.addView(holder.arrows);

            }

            if (mode.equals("WALKING")){
                distance += step.distance.value;
            }

        }

        ImageView lastArrow = (ImageView) ll.findViewById(lastView);
        if (lastArrow != null) {
            lastArrow.setVisibility(View.GONE);
        }

        holder.departureTime = new TextView(mContext);
        holder.arrivalTime = new TextView(mContext);
        holder.departureTime.setLayoutParams(vp);
        holder.arrivalTime.setLayoutParams(vp);

        holder.arrivalTime.setText(leg.arrivalTime.text);
        holder.departureTime.setText(leg.DepartureTime.text + " to ");

        holder.walkDistance = new TextView(mContext);
        holder.walkDistance.setLayoutParams(vp);
        holder.walkDistance.setGravity(Gravity.END);
        holder.walkDistance.setText(Utils.convertDistance(distance));

        lw.addView(holder.departureTime);
        lw.addView(holder.arrivalTime);

        holder.walkImage = new ImageView(mContext);
        holder.walkImage.setMaxHeight(50);
        holder.walkImage.setMaxWidth(50);
        holder.walkImage.setLayoutParams(vp);
        Glide.with(mContext)
                .load("")
                .placeholder(R.drawable.ic_maps_directions_walk)
                .centerCrop()
                .into(holder.walkImage);
        lw.addView(holder.walkImage);

        lw.addView(holder.walkDistance);

    }

    @Override
    public int getItemCount() {
        return mRoutesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView duration;
        public ImageView walkImage;
        public ImageView imageView;
        public ImageView arrows;
        public LinearLayout linearLayout;
        public TextView busName;
        public TextView walkDistance;
        public TextView departureTime, arrivalTime;

        public ViewHolder(View view){
            super(view);
            duration = (TextView) view.findViewById(R.id.journey_time);
            linearLayout = (LinearLayout) view.findViewById(R.id.parent_layout);
        }
    }

}
