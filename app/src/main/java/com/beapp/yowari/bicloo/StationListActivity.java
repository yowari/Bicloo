package com.beapp.yowari.bicloo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;

import com.beapp.yowari.bicloo.adapter.StationRecyclerAdapter;
import com.beapp.yowari.bicloo.model.Station;
import com.beapp.yowari.bicloo.service.StationFetcher;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StationListActivity extends AppCompatActivity {

    private GoogleMap mMap;

    private StationRecyclerAdapter mStationRecyclerAdapter;
    private AlertDialog mFilterOptionDialog;
    private TextView mResultsStatsText;

    // Filter parameters
    private String mQuery = "";
    private StationRecyclerAdapter.FilterOption mFilterOption = new StationRecyclerAdapter.FilterOption(false, false, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeMap(savedInstanceState);
        initializeStationsRecycler();
        initializeFilterOptionDialog();

        // Stations info results stats
        mResultsStatsText = (TextView)findViewById(R.id.stations_info_text);

        // Refresh stations list
        updateStationsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_station_list, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                updateStationsList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mQuery = query;
                updateStationsList();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            mFilterOptionDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeMap(Bundle savedInstanceState) {
        MapView mapView = (MapView)findViewById(R.id.stations_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                try {
                    mMap.setMyLocationEnabled(true);
                } catch(SecurityException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeStationsRecycler() {
        RecyclerView recyclerStations = (RecyclerView)findViewById(R.id.list_stations);
        recyclerStations.setLayoutManager(new LinearLayoutManager(this));

        final List<Station> stations = new ArrayList<Station>();

        try {
            StationFetcher.getInstance().fetchStations(new StationFetcher.StationFetchListener() {
                @Override
                public void onStationsFetchEnd(final List<Station> stations) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mStationRecyclerAdapter.setStations(stations);
                            updateStationsList();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        mStationRecyclerAdapter = new StationRecyclerAdapter(this, stations);
        recyclerStations.setAdapter(mStationRecyclerAdapter);
        mStationRecyclerAdapter.setComparator(new Station.AlphabeticalComparator());
    }

    private void initializeFilterOptionDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.filter_dialog, null);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Filter options");
        dialog.setView(alertLayout);

        final CheckBox availableBikesCheckbox = alertLayout.findViewById(R.id.available_bikes_checkbox);
        final CheckBox availableStandsCheckbox = alertLayout.findViewById(R.id.available_stands_checkbox);
        final CheckBox openStationCheckbox = alertLayout.findViewById(R.id.open_station_checkbox);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mFilterOption = new StationRecyclerAdapter.FilterOption(
                        availableBikesCheckbox.isChecked(),
                        availableStandsCheckbox.isChecked(),
                        openStationCheckbox.isChecked());
                updateStationsList();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        mFilterOptionDialog = dialog.create();
    }

    /**
     * Filter and construct the new stations list
     */
    private void updateStationsList() {
        mStationRecyclerAdapter.filter(mQuery, mFilterOption);
        mResultsStatsText.setText(mStationRecyclerAdapter.getItemCount() + " Stations");

        if (mMap != null) {
            mMap.clear();
            for (Station station: mStationRecyclerAdapter.getStations()) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(station.getLat(), station.getLng()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }
}
