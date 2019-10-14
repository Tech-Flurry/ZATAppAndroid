package com.sparkers.zatappdriver.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.sparkers.zatappdriver.Common.Common;
import com.sparkers.zatappdriver.Helpers.LoadingDialog;
import com.sparkers.zatappdriver.Model.History;
import com.sparkers.zatappdriver.Model.PaymentDetails;
import com.sparkers.zatappdriver.Model.Ride;
import com.sparkers.zatappdriver.R;
import com.sparkers.zatappdriver.RecyclerView.ClickListener;
import com.sparkers.zatappdriver.RecyclerView.historyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class TripHistory extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView rvHistory;
    historyAdapter adapter;
    ArrayList<History> listData;
    LoadingDialog loadingDialog;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        loadingDialog= new LoadingDialog(TripHistory.this);
        queue= Volley.newRequestQueue(TripHistory.this);
        initToolbar();
        initRecyclerView();
        listData = new ArrayList<>();
        adapter = new historyAdapter(this, listData, new ClickListener() {
            @Override
            public void onClick(View view, int index) {
                Intent i= new Intent(TripHistory.this, TripDetail.class);
                i.putExtra("History",listData.get(index));
                startActivity(i);
            }
        });
        rvHistory.setAdapter(adapter);
        getHistory();
    }

    private void getHistory(){
        //gets the completed rides of the driver
        String requestUrl=Common.ZAT_API_HOST+"drivers/"+Common.userID+"/GetCompletedRides";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, requestUrl, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++){
                    try {
                        final Ride ride= new Ride(response.getJSONObject(i));
                        String pickUpAddressRequestUrl="https://maps.googleapis.com/maps/api/geocode/json?latlng="+ride.getPickUpLocation().latitude+","+ride.getPickUpLocation().longitude+"&key="+getString(R.string.google_maps_key);
                        JsonObjectRequest pickUpAddressRequest= new JsonObjectRequest(Request.Method.GET, pickUpAddressRequestUrl, (String) null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    final String pickUpAddress = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                                    String dropAddressRequestUrl="https://maps.googleapis.com/maps/api/geocode/json?latlng="+ride.getDestination().latitude+","+ride.getDestination().longitude+"&key="+getString(R.string.google_maps_key);
                                    JsonObjectRequest dropAddressRequest= new JsonObjectRequest(Request.Method.GET, dropAddressRequestUrl, (String) null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                final String dropAddress = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                                                String paymentSummaryUrl=Common.ZAT_API_HOST+"Rides/"+ride.getId()+"/GetPaymentSummary";
                                                JsonObjectRequest totalAmountRequest=new JsonObjectRequest(Request.Method.GET, paymentSummaryUrl, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        PaymentDetails paymentDetails= new PaymentDetails(response);
                                                        final DateFormat format= new SimpleDateFormat("dd MMM yyyy '-' hh:mm a");
                                                        History history = new History(pickUpAddress, dropAddress, Common.timeSpanString(ride.getBookingTime(), ride.getDropOffTime()), ride.getId(), format.format(ride.getBookingTime()), ride.getRider().getName(), paymentDetails);
                                                        listData.add(history);
                                                        Collections.sort(listData, new Comparator<History>() {
                                                            @Override
                                                            public int compare(History history, History t1) {
                                                                try {
                                                                    Date startDate=format.parse(history.getTripDate());
                                                                    Date endDate=format.parse(t1.getTripDate());
                                                                    return startDate.compareTo(endDate);
                                                                } catch (ParseException e) {

                                                                    return 0;
                                                                }
                                                            }
                                                        });
                                                        Collections.reverse(listData);
                                                        adapter.notifyDataSetChanged();
                                                        loadingDialog.dismiss();
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                    }
                                                });
                                                queue.add(totalAmountRequest);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                                    queue.add(dropAddressRequest);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        queue.add(pickUpAddressRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("getHistory",error.getMessage());
            }
        });
        queue.add(request);
        loadingDialog.show();
    }

    private void initRecyclerView(){
        rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvHistory.setLayoutManager(layoutManager);
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Rides History");

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
