package com.example.www.transit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.www.transit.R;
import com.example.www.transit.model.Legs;
import com.example.www.transit.model.Routes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaskaran on 21/7/16.
 */
public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {

    private List<Routes> mRoutesList = new ArrayList<>();
    private List<Legs> mLegsList = new ArrayList<>();
    public RoutesAdapter(List<Routes> routesList, List<Legs> legsList){
        this.mRoutesList = routesList;
        this.mLegsList = legsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Routes route = mRoutesList.get(position);
        final Legs leg = mLegsList.get(position);
        holder.startAddress.setText(leg.getStartAddress());
        holder.endAddress.setText(leg.getEndAddress());
        holder.departureTime.setText(leg.DepartureTime.getText());
        holder.arrivalTime.setText(leg.arrivalTime.getText());
        holder.distance.setText(leg.distance.getText());
        holder.duration.setText(leg.duration.getText());
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

        public ViewHolder(View view){
            super(view);
            startAddress = (TextView) view.findViewById(R.id.start_address);
            endAddress = (TextView) view.findViewById(R.id.end_address);
            arrivalTime = (TextView) view.findViewById(R.id.arrival_time);
            departureTime = (TextView) view.findViewById(R.id.departure_time);
            distance = (TextView) view.findViewById(R.id.distance);
            duration = (TextView) view.findViewById(R.id.duration);
        }
    }

}
