package com.example.www.transit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.www.transit.R;
import com.example.www.transit.model.Legs;
import com.example.www.transit.model.Routes;
import com.example.www.transit.utils.Utils;

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
        holder.summary.setText(route.getSummary());
        holder.distance.setText(Utils.toKms(String.valueOf(leg.distance.getValue())) + "Kms");
        holder.duration.setText(String.valueOf(leg.duration.getValue()));
    }

    @Override
    public int getItemCount() {
        return mRoutesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView summary;
        public TextView distance;
        public TextView duration;

        public ViewHolder(View view){
            super(view);
            summary = (TextView) view.findViewById(R.id.summary);
            distance = (TextView) view.findViewById(R.id.distance);
            duration = (TextView) view.findViewById(R.id.duration);
        }
    }

}
