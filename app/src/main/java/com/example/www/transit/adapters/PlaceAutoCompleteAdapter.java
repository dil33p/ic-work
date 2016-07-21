package com.example.www.transit.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaskaran on 18/7/16.
 */
public class PlaceAutoCompleteAdapter extends ArrayAdapter<AutocompletePrediction> {
    private static final String TAG = "PlaceAutocompAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);

    // Current results returned by this adapter.
    private ArrayList<AutocompletePrediction> mResultList;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mBounds;
    private AutocompleteFilter mPlaceFilter;

    public PlaceAutoCompleteAdapter(Context context, GoogleApiClient googleApiClient, LatLngBounds bounds, AutocompleteFilter filter){
        super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mPlaceFilter = filter;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    // Returns an item from the last autocomplete query.
    @Override
    public AutocompletePrediction getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        // Sets the primary and secondary text for a row.
        AutocompletePrediction item = getItem(position);

        TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) row.findViewById(android.R.id.text2);
        textView1.setText(item.getPrimaryText(STYLE_BOLD));
        textView2.setText(item.getSecondaryText(STYLE_BOLD));

        return row;
    }

    /**
     * @return filter for the current set of autocomplete results.
     */

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null){
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint);
                    if (mResultList != null){
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0){
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                }else{
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the Autocomplete Textview
                // when clicked.
                if (resultValue instanceof AutocompletePrediction){
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                }
                return super.convertResultToString(resultValue);
            }
        };

    }

    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint){
        if (mGoogleApiClient.isConnected()){
            Log.i(TAG, "Starting autocomplete query for: " + constraint);
            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi.getAutocompletePredictions(
                            mGoogleApiClient,
                            constraint.toString(),
                            mBounds,
                            mPlaceFilter
                    );
            AutocompletePredictionBuffer autocompletePredictions = results.await(
                    60,
                    TimeUnit.SECONDS
            );

            // Confirm the query completed successfully, otherwiser return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()){
                Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i(TAG, "Query completed Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        }
        Log.e(TAG, "Google API client is not connected");
        return null;
    }

}
