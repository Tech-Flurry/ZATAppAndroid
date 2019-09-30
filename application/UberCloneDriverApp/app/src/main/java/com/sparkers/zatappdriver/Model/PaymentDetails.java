package com.sparkers.zatappdriver.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails {
    private double totalFare, gst, serviceCharges, discount, gTotal, driverAmount;

    public PaymentDetails(double totalFare, double gst, double serviceCharges, double discount, double gTotal, double driverAmount) {
        this.totalFare = totalFare;
        this.gst = gst;
        this.serviceCharges = serviceCharges;
        this.discount = discount;
        this.gTotal = gTotal;
        this.driverAmount = driverAmount;
    }

    public PaymentDetails (JSONObject response){
        try {
            totalFare=response.getDouble("TotalFare");
            gst=response.getDouble("GST");
            serviceCharges=response.getDouble("ServiceCharges");
            discount=response.getDouble("Discount");
            gTotal=response.getDouble("GTotal");
            driverAmount=response.getDouble("DriverAmount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public double getTotalFare() {
        return totalFare;
    }

    public double getGst() {
        return gst;
    }

    public double getServiceCharges() {
        return serviceCharges;
    }

    public double getDiscount() {
        return discount;
    }

    public double getgTotal() {
        return gTotal;
    }

    public double getDriverAmount() {
        return driverAmount;
    }
}
