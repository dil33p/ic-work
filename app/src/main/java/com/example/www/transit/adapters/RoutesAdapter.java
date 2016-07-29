package com.example.www.transit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Routes route = mRoutesList.get(position);
        final Legs leg = mLegsList.get(position);
        //final Steps step = mStepsList.get(position);
        holder.startAddress.setText(leg.getStartAddress());
        holder.endAddress.setText(leg.getEndAddress());
        holder.departureTime.setText(leg.DepartureTime.getText());
        holder.arrivalTime.setText(leg.arrivalTime.getText());
        holder.distance.setText(leg.distance.getText());
        holder.duration.setText(leg.duration.getText());
        List<Steps> steps = leg.getSteps();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Routes route1 = mRoutesList.get(position);
                if (mAdapterCallback != null){
                    mAdapterCallback.onItemSelected(route);
                }
            }
        });
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        holder.linearLayout.addView(ll);
        String mode = "TRANSIT";
        for (final Steps step : steps){
            Log.i(TAG, step.getTravelMode().toString() + position);
            /* mode += step.getTravelMode().toString() + " " + " > " + " " ;
            holder.travelMode.setText(mode);*/
            mode = step.getTravelMode().toString();
            holder.imageView = new ImageView(mContext);

            holder.imageView.setLayoutParams(vp);
            holder.imageView.setMaxHeight(50);
            holder.imageView.setMaxWidth(50);
            //holder.imageView.setImageResource(Utils.getIconResource(mode));
            Glide.with(mContext)
                    .load("")
                    .placeholder(Utils.getIconResource(mode))
                    .centerCrop()
                    .into(holder.imageView);
            ll.addView(holder.imageView);

        }

    }

    @Override
    public int getItemCount() {
        return mRoutesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView startAddress;
        public TextView endAddress;
        public TextView arrivalTime;
        public TextView departureTime;
        public TextView distance;
        public TextView duration;
        //public TextView travelMode;
        public ImageView imageView;
        public LinearLayout linearLayout;
        public ViewHolder(View view){
            super(view);
            startAddress = (TextView) view.findViewById(R.id.start_address);
            endAddress = (TextView) view.findViewById(R.id.end_address);
            arrivalTime = (TextView) view.findViewById(R.id.arrival_time);
            departureTime = (TextView) view.findViewById(R.id.departure_time);
            distance = (TextView) view.findViewById(R.id.distance);
            duration = (TextView) view.findViewById(R.id.duration);
            //travelMode = (TextView) view.findViewById(R.id.travel_mode);
            linearLayout = (LinearLayout) view.findViewById(R.id.parent_layout);
        }
    }

}
