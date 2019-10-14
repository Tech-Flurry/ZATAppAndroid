package com.sparkers.zatappdriver.Model;

import com.sparkers.zatappdriver.Common.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class MobileTransaction implements Serializable {
    private long transactionId;
    private String referenceNumber, serviceProviderName;
    private double amount;
    private Date transactionDate;
    private Driver driver;
    private boolean isVerified;

    public MobileTransaction(JSONObject response) {
        try {
            transactionId = response.getLong("TransactionId");
            referenceNumber = response.getString("ReferenceNumber");
            isVerified = response.getBoolean("IsVerified");
            serviceProviderName = response.getString("MobileAccountServiceProviderName");
            amount = response.getDouble("Amount");
            driver = new Driver(response.getJSONObject("Driver"));
            transactionDate = Common.jsonDateFormat.parse(response.getString("TransactionRegisteredTime"));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public long getTransactionId() {
        return transactionId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
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

    public boolean isVerified() {
        return isVerified;
    }
}
