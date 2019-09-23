package com.sparkers.zatappdriver.Model;


import org.json.JSONException;
import org.json.JSONObject;

public class Promo {
    private int id;
    private int discount;
    private String code;
    private boolean isOpen;
    public Promo(JSONObject response){
        try {
            id=response.getInt("PromoId");
            discount=response.getInt("DiscountPercent");
            code=response.getString("Code");
            isOpen=response.getBoolean("IsOpen");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
