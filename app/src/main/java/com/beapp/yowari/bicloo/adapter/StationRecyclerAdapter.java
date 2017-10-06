package com.beapp.yowari.bicloo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beapp.yowari.bicloo.R;
import com.beapp.yowari.bicloo.StationActivity;
import com.beapp.yowari.bicloo.model.Station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StationRecyclerAdapter extends RecyclerView.Adapter<StationRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private List<Station> mAllStations;
    private List<Station> mStations;

    private Comparator<Station> mComparator;

    public StationRecyclerAdapter(Context context, List<Station> stations) {
        mContext = context;
        mAllStations = stations;
        mStations = new ArrayList<Station>(stations);

        if (mComparator != null)
            Collections.sort(mStations, mComparator);

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void filter(String query, FilterOption option) {
        mStations.clear();

        if (query.isEmpty() && !option.mOnlyIfAvailableBikes && !option.mOnlyIfAvailableStands && !option.mOnlyIfOpenStation) {
            mStations.addAll(mAllStations);
        } else {
            query = query.toLowerCase();

            for (Station station: mAllStations) {
                if ((station.getName().toLowerCase().contains(query) || station.getAddress().toLowerCase().contains(query)) &&
                        ((option.mOnlyIfAvailableBikes && station.getAvailableBikes() > 0) || !option.mOnlyIfAvailableBikes) &&
                        ((option.mOnlyIfAvailableStands && station.getAvailableBikeStands() > 0) || !option.mOnlyIfAvailableStands) &&
                        ((option.mOnlyIfOpenStation && station.isOpen()) || !option.mOnlyIfOpenStation)) {
                    mStations.add(station);
                }
            }
        }

        if (mComparator != null)
            Collections.sort(mStations, mComparator);

        notifyDataSetChanged();
    }

    public void setStations(List<Station> stations) {
        mAllStations = stations;
        mStations = new ArrayList<Station>(stations);

        if (mComparator != null)
            Collections.sort(mStations, mComparator);

        notifyDataSetChanged();
    }

    public List<Station> getStations() {
        return mStations;
    }

    public void setComparator(Comparator<Station> comparator) {
        mComparator = comparator;

        if (mComparator != null)
            Collections.sort(mStations, mComparator);

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_station_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Station station = mStations.get(position);
        holder.mPosition = position;
        holder.mTextStationName.setText(station.getName());
        holder.mTextStationAddress.setText(station.getAddress());
        holder.mTextStationOpen.setText("Status: " + (station.isOpen() ? "Open" : "Closed"));
        holder.mTextStationStands.setText(station.getAvailableBikeStands() + " stands");
        holder.mTextStationBikes.setText(station.getAvailableBikes() + " bikes");
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public int mPosition;
        public final TextView mTextStationName;
        public final TextView mTextStationAddress;
        public final TextView mTextStationOpen;
        public final TextView mTextStationStands;
        public final TextView mTextStationBikes;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextStationName = (TextView)itemView.findViewById(R.id.text_station_name);
            mTextStationAddress = (TextView)itemView.findViewById(R.id.text_station_address);
            mTextStationBikes = (TextView)itemView.findViewById(R.id.text_station_bikes);
            mTextStationStands = (TextView)itemView.findViewById(R.id.text_station_stands);
            mTextStationOpen = (TextView)itemView.findViewById(R.id.text_station_open);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, StationActivity.class);
                    intent.putExtra(StationActivity.STATION, mStations.get(mPosition));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public static class FilterOption {
        private boolean mOnlyIfAvailableBikes;
        private boolean mOnlyIfAvailableStands;
        private boolean mOnlyIfOpenStation;

        public FilterOption(boolean onlyIfAvailableBikes, boolean onlyIfAvailableStands, boolean onlyIfOpenStation) {
            mOnlyIfAvailableBikes = onlyIfAvailableBikes;
            mOnlyIfAvailableStands = onlyIfAvailableStands;
            mOnlyIfOpenStation = onlyIfOpenStation;
        }
    }
}
