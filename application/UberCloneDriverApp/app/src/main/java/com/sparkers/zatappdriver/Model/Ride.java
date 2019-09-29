package com.sparkers.zatappdriver.Model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Ride {
    private long id;
    private Rider rider;
    private Driver driver;
    private LatLng destination, pickUpLocation, dropOffLocation;
    private boolean isEnded, isCanceled;
    private Promo promo;
    private Route route;
    private Date bookingTime, pickUpTime, dropOffTime;
    private String VehicleType;

    public Ride(JSONObject response){
        try {
            id=response.getLong("RideId");
            rider=new Rider(response.getJSONObject("Rider"));
            driver=new Driver(response.getJSONObject("Driver"));
            destination= new LatLng(response.getJSONObject("Destination").getDouble("Latitude"),
                    response.getJSONObject("Destination").getDouble("Longitude"));
            pickUpLocation=new LatLng(response.getJSONObject("PickUpLocation").getDouble("Latitude"),
                    response.getJSONObject("PickUpLocation").getDouble("Longitude"));
            dropOffLocation=new LatLng(response.getJSONObject("DropOffLocation").getDouble("Latitude"),
                    response.getJSONObject("DropOffLocation").getDouble("Longitude"));
            isEnded=response.getBoolean("IsEnded");
            isCanceled=response.getBoolean("IsCanceled");
            promo= new Promo(response.getJSONObject("ActivePromo"));
            route= new Route(response.getJSONObject("Route"));
            //bookingTime=new Date(response.getString("BookingTime"));
            //pickUpTime=new Date(response.getString("PickUpTime"));
            //dropOffTime= new Date(response.getString("DropOffTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public LatLng getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(LatLng pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public LatLng getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(LatLng dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Date bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Date getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(Date pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Date getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(Date dropOffTime) {
        this.dropOffTime = dropOffTime;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }
}
