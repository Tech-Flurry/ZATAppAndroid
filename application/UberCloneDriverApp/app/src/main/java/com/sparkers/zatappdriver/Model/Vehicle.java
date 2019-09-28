package com.sparkers.zatappdriver.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Vehicle {
    private int id;
    private boolean isAc;
    private String registrationNumber, model, color, vehicleType;
    private short engine;
    private Driver driver;

    public Vehicle(int id, boolean isAc, String registrationNumber, String model, String color, String vehicleType, short engine) {
        this.id = id;
        this.isAc = isAc;
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.color = color;
        this.vehicleType = vehicleType;
        this.engine = engine;
    }
    public Vehicle(JSONObject response){
        try {
            driver=new Driver(response.getJSONObject("Driver"));
            engine=(short)response.getInt("EngineCC");
            registrationNumber=response.getJSONObject("RegisterationNumber").getString("FormattedNumber");
            model=response.getString("Model");
            color="#"+Integer.toHexString(response.getInt("VehicleColor"));
            isAc=response.getBoolean("IsAC");
            vehicleType=response.getJSONObject("Type").getString("Name");
            id=response.getInt("VehicleId");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAc() {
        return isAc;
    }

    public void setAc(boolean ac) {
        isAc = ac;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public short getEngine() {
        return engine;
    }

    public void setEngine(short engine) {
        this.engine = engine;
    }
}
