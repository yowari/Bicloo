package com.beapp.yowari.bicloo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * La classe Station représente les données d'une station.
 */
public class Station implements Parcelable {
    private int number;
    private String name;
    private String address;

    // geographic coordinates of the station
    private double lat;
    private double lng;

    private boolean open;
    private int bikeStands;
    private int availableBikeStands;
    private int availableBikes;

    /**
     * Constructeur de Station
     *
     * @param number Le numéro de la station
     * @param name Le nom de la station
     * @param address Adresse indicative de la station
     * @param lat Valeur angulaire nord-sud de la station
     * @param lng Valeur angulaire est-ouest de la station
     * @param open "true" si la station est ouverte
     * @param bikeStands Le nombre de points d'attache opérationnels
     * @param availableBikeStands Le nombre de points d'attache disponibles pour y ranger un vélo
     * @param availableBikes Le nombre de vélos disponibles et opérationnels
     */
    public Station(int number,
                   String name,
                   String address,
                   double lat,
                   double lng,
                   boolean open,
                   int bikeStands,
                   int availableBikeStands,
                   int availableBikes) {
        this.number = number;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.open = open;
        this.bikeStands = bikeStands;
        this.availableBikeStands = availableBikeStands;
        this.availableBikes = availableBikes;
    }

    public Station(Parcel source) {
        number = source.readInt();
        name = source.readString();
        address = source.readString();
        lat = source.readDouble();
        lng = source.readDouble();
        open = source.readByte() == 1;
        bikeStands = source.readInt();
        availableBikeStands = source.readInt();
        availableBikes = source.readInt();
    }

    /**
     * Le numéro de la station. Attention, ce n'est pas un id, ce numéro n'est unique qu'au sein
     * d'un contrat.
     *
     * @return Le numéro de la station
     */
    public int getNumber() {
        return number;
    }

    /**
     * le nom de la station.
     *
     * @return Le nom de la station
     */
    public String getName() {
        return name;
    }

    /**
     * Adresse indicative de la station, les données étant brutes, parfois il s'agit plus d'un
     * commentaire que d'une adresse.
     *
     * @return Adresse indicative de la station
     */
    public String getAddress() {
        return address;
    }

    /**
     * La latitude de la station
     *
     * @return Valeur angulaire nord-sud de la station
     */
    public double getLat() {
        return lat;
    }

    /**
     * La longitude de la station
     *
     * @return Valeur angulaire est-ouest de la station
     */
    public double getLng() {
        return lng;
    }

    /**
     * Indique l'état de la station, peut être CLOSED ou OPEN.
     *
     * @return "true" si la station est ouverte
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Le nombre de points d'attache opérationnels.
     *
     * @return Le nombre de points d'attache opérationnels
     */
    public int getBikeStands() {
        return bikeStands;
    }

    /**
     * Le nombre de points d'attache disponibles pour y ranger un vélo
     *
     * @return Le nombre de points d'attache disponibles pour y ranger un vélo
     */
    public int getAvailableBikeStands() {
        return availableBikeStands;
    }

    /**
     * Le nombre de vélos disponibles et opérationnels
     *
     * @return Le nombre de vélos disponibles et opérationnels
     */
    public int getAvailableBikes() {
        return availableBikes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeByte((byte)(open ? 1 : 0));
        dest.writeInt(bikeStands);
        dest.writeInt(availableBikeStands);
        dest.writeInt(availableBikes);
    }

    public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {

        @Override
        public Station createFromParcel(Parcel source) {
            return new Station(source);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    public static class LocationComparator implements Comparator<Station> {

        private double lat;
        private double lng;

        public LocationComparator(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public int compare(Station stationA, Station stationB) {
            double diffA = Math.abs(this.lat - stationA.getLat()) + Math.abs(this.lng - stationA.getLng());
            double diffB = Math.abs(this.lat - stationB.getLat()) + Math.abs(this.lng - stationB.getLng());
            double diff = diffA - diffB;

            if (diff < 0) {
                return -1;
            } else if (diff == 0) {
                return 0;
            } else {
                return 1;
            }
        }

        public void setPosition(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }

    public static class AlphabeticalComparator implements Comparator<Station> {

        @Override
        public int compare(Station stationA, Station stationB) {
            return stationA.getName().compareTo(stationB.getName());
        }
    }

}
