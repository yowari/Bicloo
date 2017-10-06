package com.beapp.yowari.bicloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.beapp.yowari.bicloo.model.Station;

public class StationActivity extends AppCompatActivity {

    public static final String STATION = "com.beapp.yowari.bicloo.StationActivity.STATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        TextView address = (TextView)findViewById(R.id.station_address);
        TextView open = (TextView)findViewById(R.id.station_open);
        TextView bikes = (TextView)findViewById(R.id.station_bikes);
        TextView stands = (TextView)findViewById(R.id.station_stands);

        Intent intent = getIntent();
        Station station = intent.getParcelableExtra(STATION);

        setTitle(station.getName());

        address.setText("Address: " + station.getAddress());
        open.setText("Status: " + (station.isOpen() ? "OPEN" : "CLOSED"));
        bikes.setText("Bikes: " + station.getAvailableBikes());
        // This must be the work of an ennemy STANDO!
        stands.setText("Stands: " + station.getAvailableBikeStands());
    }
}
