package com.sparkers.zatappdriver.Common;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.sparkers.zatappdriver.Interfaces.IFCMService;
import com.sparkers.zatappdriver.Interfaces.googleAPIInterface;
import com.sparkers.zatappdriver.Model.Driver;
import com.sparkers.zatappdriver.Model.Ride;
import com.sparkers.zatappdriver.Retrofit.FCMClient;
import com.sparkers.zatappdriver.Retrofit.RetrofitClient;

import java.util.Date;

public class Common {
    public static final String driver_tbl="Drivers";
    public static final String user_driver_tbl="DriversInformation";
    public static final String history_driver = "DriverHistory";
    public static final String history_rider = "RiderHistory";
    public static final String user_rider_tbl="RidersInformation";
    public static final String pickup_request_tbl="PickupRequest";
    public static final String token_tbl="Tokens";
    public static Driver currentDriver;
    public static long userID;
    public static final int PICK_IMAGE_REQUEST = 9999;
    public static final String ZAT_API_HOST="http://zatapp.azurewebsites.net/api/";
    public static final LatLngBounds APPLICATION_SERVICE_BOUNDS;
    public static Double currentLat;
    public static Double currentLng;
    public static Ride currentRide;
    public static final String baseURL="https://maps.googleapis.com";
    public static final String fcmURL="https://fcm.googleapis.com/";
    static{
        APPLICATION_SERVICE_BOUNDS=new LatLngBounds(new LatLng(31.299430,73.803619), new LatLng(31.330596,73.800527));
    }
    public static double baseFare=2.55;
    private static double timeRate=0.35;
    private static double distanceRate=1.75;
    public static double formulaPrice(double km, double min){
        return baseFare+(distanceRate*km)+(timeRate*min);
    }
    public static googleAPIInterface getGoogleAPI(){
        return RetrofitClient.getClient(baseURL).create(googleAPIInterface.class);
    }
    public static IFCMService getFCMService(){
        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }

    public static String timeSpanString(Date date1, Date date2) {
        double sec = (date2.getTime() - date1.getTime()) / 1000;
        double min = sec / 60;
        double hr = min / 60;
        if (min < 1) {
            return String.format("%.2f sec.", sec);
        } else if (hr < 1) {
            return String.format("%.2f min.", min);
        } else {
            return String.format("%.2f hr.", hr);
        }
    }
}
