package com.sparkers.zatappdriver.Model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class Driver {
    private String name, cnicNumber, fullName;
    private String phone;
    private String avatarUrl;
    private String rating;
    private Vehicle vehicle;
    private double creditLimit, balance;
    private boolean isBooked, isCleared, isBlocked, isActive;
    private long id;
    private LatLng lastLocation;
    public Driver(){

    }
    public Driver(JSONObject response){
        try {
            name =response.getJSONObject("FullName").getString("FirstName");
            cnicNumber=response.getString("CNIC_Number");
            fullName=response.getJSONObject("FullName").getString("FirstName")+" "+response.getJSONObject("FullName").getString("LastName");
            phone=response.getJSONObject("ContactNumber").getString("PhoneNumberFormat");
            //avatarUrl=response.getString("Picture");
            rating=response.getDouble("TotalRating")+"";
            id=response.getLong("UserId");
            creditLimit=response.getDouble("CreditLimit");
            balance=response.getDouble("Balance");
            isBooked=response.getBoolean("IsBooked");
            isCleared=response.getBoolean("IsCleared");
            isBlocked=response.getBoolean("IsBlocked");
            isActive=response.getBoolean("IsActive");
            lastLocation= new LatLng(response.getJSONObject("LastLocation").getDouble("Latitude"),
                    response.getJSONObject("LastLocation").getDouble("Longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Driver(String name, String phone, double rating) {
        this.name = name;
        this.phone = phone;
        this.rating = rating +"";

    }

    public Driver(String name, String phone, String avatarUrl, double rating) {
        this.name = name;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.rating = rating+"";
    }


    public Driver(String name, String phone, String avatarUrl, double rating, Vehicle vehicle) {
        this.name = name;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.rating = rating+"";
        this.vehicle = vehicle;
    }

    public String getCnicNumber() {
        return cnicNumber;
    }

    public void setCnicNumber(String cnicNumber) {
        this.cnicNumber = cnicNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public boolean isCleared() {
        return isCleared;
    }

    public void setCleared(boolean cleared) {
        isCleared = cleared;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
