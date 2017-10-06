package com.beapp.yowari.bicloo.service;

import android.util.Log;

import com.beapp.yowari.bicloo.model.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Service permettant de récuperer les données de JCDecaux
 */
public class StationFetcher {

    public static final String ENDPOINT = "https://api.jcdecaux.com/vls/v1/stations";
    public static final String CONTRACT = "Nantes";
    public static final String API_KEY = "8c5504cb15832868d87218d11842b44bf51883c0";

    private static final OkHttpClient client = new OkHttpClient();

    private static StationFetcher instance;

    private StationFetcher() {
    }

    public static StationFetcher getInstance() {
        if (instance == null) {
            instance = new StationFetcher();
        }
        return instance;
    }

    /**
     * La methode fetchStations récupère les données de JCDecaux et notifie le listener une fois
     * fait. Les données récupérés sont convertis en POJO (parse), les objet sont de classe
     * Station.
     *
     * @param listener callback une fois le les données récupérées
     * @throws IOException
     */
    public void fetchStations(final StationFetchListener listener) throws IOException {
        String url = HttpUrl.parse(ENDPOINT).newBuilder()
                .addQueryParameter("contract", CONTRACT)
                .addQueryParameter("apiKey", API_KEY)
                .build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    List<Station> stations = parseStations(new JSONArray(response.body().string()));
                    listener.onStationsFetchEnd(stations);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<Station> parseStations(JSONArray stations) throws JSONException {
        List<Station> stationList = new ArrayList<Station>();

        for (int i = 0; i < stations.length(); i++) {
            JSONObject jsonStation = stations.getJSONObject(i);
            stationList.add(parseStation(jsonStation));
        }

        return stationList;
    }

    private Station parseStation(JSONObject station) throws JSONException {
        return new Station(
                station.getInt("number"),
                station.getString("name"),
                station.getString("address"),
                station.getJSONObject("position").getDouble("lat"),
                station.getJSONObject("position").getDouble("lng"),
                station.getString("status").equals("OPEN"),
                station.getInt("bike_stands"),
                station.getInt("available_bike_stands"),
                station.getInt("available_bikes")
        );
    }

    public interface StationFetchListener {
        void onStationsFetchEnd(List<Station> stations);
    }
}
