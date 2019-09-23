package com.sparkers.zatappdriver.Model;

public class User{
    private String name;
    private String phone;
    private String avatarUrl;
    private String rating;
    private Vehicle vehicle;

    public User(){

    }

    public User(String name, String phone, double rating) {
        this.name = name;
        this.phone = phone;
        this.rating = rating +"";

    }

    public User(String name, String phone, String avatarUrl, double rating) {
        this.name = name;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.rating = rating+"";
    }


    public User(String name, String phone, String avatarUrl, double rating, Vehicle vehicle) {
        this.name = name;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.rating = rating+"";
        this.vehicle = vehicle;
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
