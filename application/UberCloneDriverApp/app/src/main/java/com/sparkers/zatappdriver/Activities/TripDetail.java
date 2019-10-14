package com.sparkers.zatappdriver.Activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sparkers.zatappdriver.Common.Common;
import com.sparkers.zatappdriver.Helpers.LoadingDialog;
import com.sparkers.zatappdriver.Model.History;
import com.sparkers.zatappdriver.Model.Ride;
import com.sparkers.zatappdriver.R;

import org.json.JSONObject;

import java.util.Calendar;

public class TripDetail extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView txtDate, txtTotalAmount, txtDriverAmount, txtZATAmount,txtDiscount,txtTime, txtDistance, txtEstimatedPayout, txtFrom, txtTo;
    private History history;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        initToolbar();
        loadingDialog=new LoadingDialog(TripDetail.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        history=(History)getIntent().getSerializableExtra("History");
        txtDate= findViewById(R.id.txtDate);
        txtTotalAmount = findViewById(R.id.txtFee);
        txtDriverAmount = findViewById(R.id.txtDriverFare);
        txtZATAmount=findViewById(R.id.txtZatAmount);
        txtDiscount=findViewById(R.id.txtDiscount);
        txtTime= findViewById(R.id.txtDuration);
        txtDistance= findViewById(R.id.txtDistance);
        txtFrom=findViewById(R.id.txtFrom);
        txtTo=findViewById(R.id.txtTo);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style_map));
        settingInformation();
    }

    private void settingInformation() {
        if(getIntent()!=null) {
            txtDate.setText(history.getTripDate());
            txtTotalAmount.setText(String.format("Rs. %.2f", history.getPaymentDetails().getgTotal()));
            txtDriverAmount.setText(String.format("Rs. %.2f", history.getPaymentDetails().getDriverAmount()));
            txtDiscount.setText(String.format("Rs. %.2f", history.getPaymentDetails().getDiscount()));
            txtZATAmount.setText(String.format("Rs. %.2f", history.getPaymentDetails().getServiceCharges()));
            txtTime.setText(String.format("%s min", history.getTime()));
            txtTo.setText(history.getEndAddress());
            txtFrom.setText(history.getStartAddress());
            getRoute();

            //add marker
            //String[] location_end = getIntent().getStringExtra("location_end").split(",");
            //LatLng dropOff = new LatLng(Double.parseDouble(location_end[0]), Double.parseDouble(location_end[1]));

            //mMap.addMarker(new MarkerOptions().position(dropOff)
              //      .title("Drop Off Here")
                //    .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker)));

            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dropOff, 12.0f));
        }
    }

    private void getRoute() {
        RequestQueue queue= Volley.newRequestQueue(TripDetail.this);
        String requestUrl= Common.ZAT_API_HOST+"Rides/"+history.getRideId();
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, requestUrl, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Ride ride= new Ride(response);
                txtDistance.setText(String.format("%.2f km", ride.getRoute().getTotalDistance()));
                //add polyline
                mMap.addPolyline(new PolylineOptions().addAll(ride.getRoute().getCordinates()).color(R.color.secondaryDarkColor));
                LatLng dropOff=ride.getRoute().getCordinates().get(ride.getRoute().getCordinates().size()-1);
                LatLng pickUp= ride.getRoute().getCordinates().get(0);
                mMap.addMarker(new MarkerOptions().position(dropOff)
                      .title("Drop Off Here")
                   .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker)));
                mMap.addMarker(new MarkerOptions().position(pickUp)
                        .title("PickUp Here")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                LatLngBounds bounds= new LatLngBounds(pickUp,dropOff);
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));
                loadingDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        loadingDialog.show();
    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ride Details");

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
