package com.sparkers.zatappdriver.Model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private List<LatLng> cordinates;
    private double totalDistance;
    public Route(JSONObject response){
        cordinates= new ArrayList<>();
        try {
            JSONArray cords= response.getJSONArray("Cordinates");

             for (int i=0; i<cords.length(); i++){
                 JSONObject cord=cords.getJSONObject(i);
                 cordinates.add(new LatLng(cord.getDouble("Latitude"),cord.getDouble("Longitude")));
             }
             totalDistance=response.getDouble("TotalDistance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<LatLng> getCordinates() {
        return cordinates;
    }

    public void setCordinates(List<LatLng> cordinates) {
        this.cordinates = cordinates;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }
}
