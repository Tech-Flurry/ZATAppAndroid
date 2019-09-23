package com.sparkers.zatappdriver.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Rider {
    private long id;
    private boolean isBlocked, isActive;
    private String name, fullName, phone;

    public Rider(JSONObject response){
        try {
            id=response.getLong("UserId");
            isBlocked=response.getBoolean("IsBlocked");
            isActive=response.getBoolean("IsActive");
            name=response.getJSONObject("FullName").getString("FirstName");
            fullName=response.getJSONObject("FullName").getString("FirstName")+" "+response.getJSONObject("FullName").getString("LastName");
            phone=response.getJSONObject("ContactNumber").getString("PhoneNumberFormat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Rider(long id, boolean isBlocked, boolean isActive, String name, String fullName, String phone) {
        this.id = id;
        this.isBlocked = isBlocked;
        this.isActive = isActive;
        this.name = name;
        this.fullName = fullName;
        this.phone = phone;
    }

    public Rider(boolean isBlocked, boolean isActive, String name, String phone) {
        this.isBlocked = isBlocked;
        this.isActive = isActive;
        this.name = name;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
