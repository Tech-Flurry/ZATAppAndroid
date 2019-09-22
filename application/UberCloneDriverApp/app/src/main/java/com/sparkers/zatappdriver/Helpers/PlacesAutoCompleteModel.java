package com.sparkers.zatappdriver.Helpers;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class PlacesAutoCompleteModel {
    private String name;
    private String address;
    private LatLng location;

    public PlacesAutoCompleteModel(String name, String address, LatLng location) {
        this.name = name;
        this.address = address;
        this.location=location;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
