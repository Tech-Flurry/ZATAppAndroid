package com.sparkers.zatappdriver.Model;

import com.sparkers.zatappdriver.Common.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class ManualPayment implements Serializable {
    private long transactionId;
    private double amount;
    private Date transactionDate;
    private Driver driver;

    public ManualPayment(JSONObject response) {
        try {
            transactionId = response.getLong("TransactionId");
            amount = response.getDouble("Amount");
            driver = new Driver(response.getJSONObject("Driver"));
            transactionDate = Common.jsonDateFormat.parse(response.getString("TransactionDateTime"));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public long getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public Driver getDriver() {
        return driver;
    }
}
