package com.example.www.transit.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.www.transit.R;
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
        Resources resources = mContext.getResources();
        float primaryTextSize = 16;
        float screenDensity = mContext.getResources().getDisplayMetrics().density;
        float calculatedPrimaryText =  primaryTextSize/screenDensity;

        final Routes route = mRoutesList.get(position);
        final Legs leg = mLegsList.get(position);
        holder.duration.setText(leg.duration.getText());
        holder.duration.setTypeface(Typefaces.get(mContext, "Roboto-Regular.ttf"));
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


        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams rp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams rp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams rp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rp.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        rp2.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        holder.linearLayout.addView(ll);
        String mode = "";

        RelativeLayout lw = new RelativeLayout(mContext);
        lw.setLayoutParams(lp);
        //lw.setOrientation(LinearLayout.HORIZONTAL);
        lw.setPadding(6,6,6,6);


        holder.linearLayout.addView(lw);


        int lastView = 0;
        long distance = 0;
        int id = 0;
        int id1 = 0;
        for (final Steps step : steps){
            id++;
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
                holder.busName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                holder.busName.setTextColor(resources.getColor(R.color.primaryText));
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

                String a = "http:" + transitDetails.line.vehicle.icon;
                System.out.println(a);

                Glide.with(mContext)
                        .load(a)
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
        holder.departureTime.setId(id1+1);
        holder.departureTime.setTypeface(Typefaces.get(mContext, "Roboto-Regular.ttf"));
        holder.departureTime.setLayoutParams(rp2);

        holder.arrivalTime = new TextView(mContext);
        rp3.addRule(RelativeLayout.RIGHT_OF, holder.departureTime.getId());
        holder.arrivalTime.setLayoutParams(rp3);
        holder.arrivalTime.setText(leg.arrivalTime.text);

        holder.departureTime.setText(leg.DepartureTime.text + " to ");

        holder.walkDistance = new TextView(mContext);
        holder.walkDistance.setLayoutParams(rp);
        holder.walkDistance.setId(id);
        holder.walkDistance.setText(Utils.convertDistance(distance));

        lw.addView(holder.departureTime);
        lw.addView(holder.arrivalTime);

        holder.walkImage = new ImageView(mContext);
        holder.walkImage.setMaxHeight(50);
        holder.walkImage.setMaxWidth(50);
        rp1.addRule(RelativeLayout.LEFT_OF, holder.walkDistance.getId());
        holder.walkImage.setLayoutParams(rp1);
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
