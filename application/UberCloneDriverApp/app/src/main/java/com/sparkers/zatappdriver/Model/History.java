package com.sparkers.zatappdriver.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class History implements Serializable {
    private String startAddress,endAddress,time,tripDate,name;
    private PaymentDetails paymentDetails;

    private long rideId;
    public History(){

    }

    public History(String startAddress, String endAddress, String time, long rideId, String tripDate, String name, PaymentDetails paymentDetails) {
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.time = time;
        this.rideId = rideId;
        this.tripDate = tripDate;
        this.name = name;
        this.paymentDetails=paymentDetails;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRideId() {
        return rideId;
    }
}
