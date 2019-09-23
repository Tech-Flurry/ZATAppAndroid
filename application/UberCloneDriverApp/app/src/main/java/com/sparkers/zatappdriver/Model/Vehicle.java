package com.sparkers.zatappdriver.Model;

public class Vehicle {
    private int id;
    private boolean isAc;
    private String registrationNumber, model, color, vehicleType;
    private short engine;

    public Vehicle(int id, boolean isAc, String registrationNumber, String model, String color, String vehicleType, short engine) {
        this.id = id;
        this.isAc = isAc;
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.color = color;
        this.vehicleType = vehicleType;
        this.engine = engine;
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
