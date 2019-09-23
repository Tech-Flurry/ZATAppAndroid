package com.sparkers.zatappdriver.Activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.SphericalUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sparkers.zatappdriver.Common.Common;
import com.sparkers.zatappdriver.Helpers.AutoCompleteAdapter;
import com.sparkers.zatappdriver.Helpers.PlacesAutoCompleteModel;
import com.sparkers.zatappdriver.Interfaces.locationListener;
import com.sparkers.zatappdriver.Messages.Errors;
import com.sparkers.zatappdriver.Messages.Message;
import com.sparkers.zatappdriver.Model.Driver;
import com.sparkers.zatappdriver.Model.Vehicle;
import com.sparkers.zatappdriver.R;
import com.sparkers.zatappdriver.Util.Location;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class DriverHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback{
    Toolbar toolbar;
    Location location=null;
    private GoogleMap mMap;
    Marker currentLocationMarker;
    GoogleSignInAccount account;
    RequestQueue queue;
    GeoFire geoFire;
    boolean markerFlag=false; //use to set zoom and position of the location marker
    boolean cardClickFlag=false;
    private GoogleApiClient mGoogleApiClient;

    private static final int REQUEST_CODE_PERMISSION=100;
    private static final int PLAY_SERVICES_REQUEST_CODE=2001;

    SupportMapFragment mapFragment;

    SwitchCompat locationSwitch=null;

    private List<LatLng> polyLineList;
    private Marker carMarker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition, currentPosition;
    private int index, next;
    private LatLng destination;
    private PolylineOptions polylineOptions, blanckPolylineOptions;
    private Polyline blackPolyline, greyPolyline;
    protected NavigationView navigationView;
    private AutoCompleteTextView autoSearchPlaces;
    private ImageButton btnMyLocation;
    private CardView cardInfo;
    private CardView cardDestinationInfo;
    private TextView txtDistance;
    private TextView txtDuration;
    private Button btnEndRide;
    StorageReference storageReference;


    private float getBearing(LatLng startPosition, LatLng endPosition) {
        double lat=Math.abs(startPosition.latitude-endPosition.latitude);
        double lng=Math.abs(startPosition.longitude-endPosition.longitude);

        if (startPosition.latitude<endPosition.latitude && startPosition.longitude<endPosition.longitude)
            return (float)(Math.toDegrees(Math.atan(lng/lat)));
        else if (startPosition.latitude>=endPosition.latitude && startPosition.longitude<endPosition.longitude)
            return (float)((90-Math.toDegrees(Math.atan(lng/lat)))+90);
        else if (startPosition.latitude>=endPosition.latitude && startPosition.longitude>=endPosition.longitude)
            return (float)(Math.toDegrees(Math.atan(lng/lat))+180);
        else if (startPosition.latitude<endPosition.latitude && startPosition.longitude>=endPosition.longitude)
            return (float)((90-Math.toDegrees(Math.atan(lng/lat)))+270);

        return -1;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_home);
        cardInfo=findViewById(R.id.cardInfo);
        cardDestinationInfo=findViewById(R.id.cardDestinationDetails);
        txtDistance=findViewById(R.id.txtDistance);
        txtDuration=findViewById(R.id.txtDuration);
        btnEndRide=findViewById(R.id.btnEndRide);
        btnEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destination=null;
                carMarker.remove();
                carMarker=null;
                blackPolyline.remove();
                greyPolyline.remove();
                TransitionManager.beginDelayedTransition(cardDestinationInfo);
                cardDestinationInfo.setVisibility(View.INVISIBLE);
            }
        });
        queue=Volley.newRequestQueue(DriverHome.this);
        verifyUserAccount();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        location=new Location(this, new locationListener() {
            @Override
            public void locationResponse(LocationResult response) {
                Common.currentLat=response.getLastLocation().getLatitude();
                Common.currentLng=response.getLastLocation().getLongitude();
                updateLocation();
                setActiveStatus(true);
                if (destination!=null){

                    if(currentLocationMarker!=null)
                        currentLocationMarker.remove();
                    if(blackPolyline!=null)
                        blackPolyline.remove();
                    if (greyPolyline!=null)
                        greyPolyline.remove();
                    getDirection();
                }else{
                    displayLocation();
                }
            }
        });
        locationSwitch=findViewById(R.id.locationSwitch);
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    location.initializeLocation();
                    btnMyLocation.setVisibility(View.VISIBLE);
                }else{
                    location.stopUpdateLocation();
                    currentLocationMarker.remove();
                    mMap.clear();
                    setActiveStatus(false);
                    btnMyLocation.setVisibility(View.INVISIBLE);
                    cardDestinationInfo.setVisibility(View.INVISIBLE);
                }
            }
        });
        //Places Auto Complete
        autoSearchPlaces=findViewById(R.id.autoSearchPlaces);
        autoSearchPlaces.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                boolean flag=false;
                if (keyEvent.getAction()==KeyEvent.ACTION_UP){
                    final AutoCompleteTextView autoCompleteTextView= (AutoCompleteTextView)view;
                    String query= autoCompleteTextView.getText().toString();
                    final ArrayList<PlacesAutoCompleteModel> lstPlaces= new ArrayList<>();
                        String placesUrl="https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+query+"&inputtype=textquery&fields=formatted_address,name,geometry/location&key="+getString(R.string.google_maps_key)+"&locationbias=rectangle:"+Common.APPLICATION_SERVICE_BOUNDS.southwest.latitude+","+Common.APPLICATION_SERVICE_BOUNDS.southwest.longitude+"|"+Common.APPLICATION_SERVICE_BOUNDS.northeast.latitude+","+Common.APPLICATION_SERVICE_BOUNDS.southwest.longitude;
                        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, placesUrl, "", new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray candidates=response.getJSONArray("candidates");
                                    for (int i=0; i<candidates.length();i++){
                                        JSONObject locationObject= candidates.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                                        LatLng location= new LatLng(locationObject.getDouble("lat"),locationObject.getDouble("lng"));
                                        lstPlaces.add(new PlacesAutoCompleteModel(candidates.getJSONObject(i).getString("name").toUpperCase(),candidates.getJSONObject(i).getString("formatted_address"),location));
                                    }
                                    AutoCompleteAdapter adapter= new AutoCompleteAdapter(lstPlaces,DriverHome.this);
                                    autoSearchPlaces.setAdapter(adapter);
                                    autoSearchPlaces.showDropDown();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Message.messageError(DriverHome.this,Errors.WITHOUT_LOCATION);
                            }
                        });
                        queue.add(request);
                    flag=true;
                }
                return flag;
            }
        });
        autoSearchPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout placeItem=(LinearLayout)view;
                destination= (LatLng)placeItem.getTag();
                autoSearchPlaces.clearFocus();
            }
        });
        //~Places Auto Complete
        btnMyLocation=findViewById(R.id.btnMyLocation);
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerFlag=false;
            }
        });
        polyLineList=new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpLocation();
    }

    private void updateLocation() {
        String locationUrl=Common.ZAT_API_HOST+"Drivers/"+Common.userID+"/UpdateLocation?Latitude="+Common.currentLat+"&Longitude="+Common.currentLng;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, locationUrl, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LocationUpdate",error.getMessage());
            }
        });
        queue.add(request);
    }

    private void setActiveStatus(boolean status) {
        String locationUrl=Common.ZAT_API_HOST+"Drivers/"+Common.userID+"/ChangeActiveStatus/"+status;
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, locationUrl, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ActiveStatus",error.getMessage());
            }
        });
        queue.add(request);
    }

    public void initDrawer(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView=navigationView.getHeaderView(0);
        TextView tvName= navigationHeaderView.findViewById(R.id.tvDriverName);
        TextView tvStars= navigationHeaderView.findViewById(R.id.tvStars);
        CircleImageView imageAvatar= navigationHeaderView.findViewById(R.id.imageAvatar);

        tvName.setText(Common.currentDriver.getName());
        if(Common.currentDriver.getRating()!=null &&
                !TextUtils.isEmpty(Common.currentDriver.getRating()))
            tvStars.setText(Common.currentDriver.getRating());

         /*if(isLoggedInFacebook)
            Picasso.get().load("https://graph.facebook.com/" + Common.userID + "/picture?width=500&height=500").into(imageAvatar);
        else if(account!=null)
            Picasso.get().load(account.getPhotoUrl()).into(imageAvatar);*/

        if(Common.currentDriver.getAvatarUrl()!=null &&
                !TextUtils.isEmpty(Common.currentDriver.getAvatarUrl()))
        Picasso.get().load(Common.currentDriver.getAvatarUrl()).into(imageAvatar);
    }

    private void loadUser(){
        String requestUrl=Common.ZAT_API_HOST+"drivers/"+Common.userID;
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, requestUrl, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Common.currentDriver = new Driver(response);
                getUserVehicle();
                initDrawer();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("loadUser",error.getMessage());
            }
        });
        queue.add(request);
    }

    private void getUserVehicle() {
        String requestUrl=Common.ZAT_API_HOST+"drivers/"+Common.userID+"/GetVehicle";
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, requestUrl, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Vehicle vehicle=new Vehicle(response);
                Common.currentDriver.setVehicle(vehicle);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("loadUser",error.getMessage());
            }
        });
        queue.add(request);
    }

    private void verifyUserAccount() {
        Common.userID=48;
        loadUser();
    }

    private void getDirection(){
        currentPosition=new LatLng(Common.currentLat, Common.currentLng);
        final String requestApi;
            requestApi="https://maps.googleapis.com/maps/api/directions/json?mode=driving&" +
                    "transit_routing_preference=less_driving&origin="+Common.currentLat+","+Common.currentLng+"&" +
                    "destination="+destination.latitude+","+destination.longitude+"&key="+getResources().getString(R.string.google_maps_key);
            Log.d("URL_MAPS", requestApi);
            JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, requestApi, "", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray routes = null;
                    try {
                        routes = response.getJSONArray("routes");
                        JSONObject route= routes.getJSONObject(0);
                        JSONObject distance=route.getJSONArray("legs").getJSONObject(0).getJSONObject("distance");
                        JSONObject duration=route.getJSONArray("legs").getJSONObject(0).getJSONObject("duration");
                        txtDistance.setText(distance.getString("text"));
                        txtDuration.setText(duration.getString("text"));
                        cardDestinationInfo.setVisibility(View.VISIBLE);
                        String encodedPolyline = route.getJSONObject("overview_polyline").getString("points");
                        polyLineList=decodePoly(encodedPolyline);
                        if (!polyLineList.isEmpty()) {
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (LatLng latLng : polyLineList)
                                builder = builder.include(latLng);
                            LatLngBounds bounds = builder.build();
                            CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                            //mMap.animateCamera(mCameraUpdate);
                            polylineOptions = new PolylineOptions();
                            polylineOptions.color(Color.GRAY);
                            polylineOptions.width(5);
                            polylineOptions.startCap(new SquareCap());
                            polylineOptions.endCap(new SquareCap());
                            polylineOptions.jointType(JointType.ROUND);
                            polylineOptions.addAll(polyLineList);
                            greyPolyline = mMap.addPolyline(polylineOptions);

                            blanckPolylineOptions = new PolylineOptions();
                            blanckPolylineOptions.color(Color.BLACK);
                            blanckPolylineOptions.width(5);
                            blanckPolylineOptions.startCap(new SquareCap());
                            blanckPolylineOptions.endCap(new SquareCap());
                            blanckPolylineOptions.jointType(JointType.ROUND);
                            blackPolyline = mMap.addPolyline(blanckPolylineOptions);

                            mMap.addMarker(new MarkerOptions().position(polyLineList.get(polyLineList.size() - 1))
                                    .title("Pickup location"));

                            final ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                            polylineAnimator.setDuration(2000);
                            polylineAnimator.setInterpolator(new LinearInterpolator());
                            polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    List<LatLng> points = greyPolyline.getPoints();
                                    int percentValue = (int) animation.getAnimatedValue();
                                    int size = points.size();
                                    int newPoints = (int) (size * (percentValue / 100.0f));

                                    List<LatLng> p = points.subList(0, newPoints);
                                    LatLng oldPosition=polyLineList.get(0);
                                    LatLng newPosition= polyLineList.get(1);
                                    blackPolyline.setPoints(p);
                                    if(carMarker!=null){
                                        carMarker.setPosition(currentPosition);
                                    }
                                    else {
                                        carMarker = mMap.addMarker(new MarkerOptions().position(currentPosition).flat(true));
                                        carMarker.setAnchor(0.5f, 0.5f);
                                        if (Common.currentDriver.getVehicle().getVehicleType().equalsIgnoreCase("car")){
                                            carMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car));
                                        }
                                        else if (Common.currentDriver.getVehicle().getVehicleType().equalsIgnoreCase("bike")){
                                            carMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
                                        }
                                        else if (Common.currentDriver.getVehicle().getVehicleType().equalsIgnoreCase("Auto-Rickshaw")){
                                            carMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.rickshaw));
                                        }
                                    }
                                        carMarker.setRotation(getBearing(oldPosition, newPosition));
                                    float zoom=mMap.getCameraPosition().zoom;
                                    LatLng position= mMap.getCameraPosition().target;
                                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(position).zoom(zoom).build()));
                                }
                            });
                            polylineAnimator.start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(request);

    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void setUpLocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_CODE_PERMISSION);
        }else{
            if (checkPlayServices()){
                //buildGoogleApiClient();
                if (locationSwitch.isChecked()){
                    displayLocation();
                }
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode!=ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_REQUEST_CODE).show();
            else {
                Message.messageError(this, Errors.NOT_SUPPORT);
                finish();
            }
            return false;
        }
        return true;
    }

    private void displayLocation(){
        if (Common.currentLat!=null && Common.currentLng!=null){
            if (locationSwitch.isChecked()) {
                LatLng center=new LatLng(Common.currentLat, Common.currentLng);
                LatLng northSide=SphericalUtil.computeOffset(center, 100000, 0);
                LatLng southSide=SphericalUtil.computeOffset(center, 100000, 180);


                LatLng currentLocation = new LatLng(Common.currentLat, Common.currentLng);
                if (currentLocationMarker != null) currentLocationMarker.remove();

                currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        .title("Your Location"));
                float zoom=15.0f;
                LatLng position=new LatLng(Common.currentLat,Common.currentLng);
                if(markerFlag){
                    zoom=mMap.getCameraPosition().zoom;
                    position=mMap.getCameraPosition().target;
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
                markerFlag=true;
            }
        }else{
            Message.messageError(this, Errors.WITHOUT_LOCATION);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style_map));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    location.onRequestPermissionResult(requestCode, permissions, grantResults);
                    if (checkPlayServices()){
                        //buildGoogleApiClient();
                        if (locationSwitch.isChecked())displayLocation();
                    }
                }

                break;
            case PLAY_SERVICES_REQUEST_CODE:
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setActiveStatus(false);
        location.stopUpdateLocation();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_trip_history:
                showTripHistory();
                break;
            case R.id.nav_car_type:
                showDialogUpdateCarType();
                break;
            case R.id.nav_update_info:
                showDialogUpdateInfo();
                break;
            case R.id.nav_change_pwd:
                if(account!=null)
                    showDialogChangePwd();
                break;
            case R.id.nav_sign_out:
                signOut();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showTripHistory() {
        Intent intent=new Intent(DriverHome.this, TripHistory.class);
        startActivity(intent);
    }

    private void showDialogUpdateCarType() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriverHome.this);
        alertDialog.setTitle("UPDATE VEHICLE TYPE");
        LayoutInflater inflater = this.getLayoutInflater();
        View carType = inflater.inflate(R.layout.layout_update_car_type, null);
        final RadioButton rbUberX=carType.findViewById(R.id.rbUberX);
        final RadioButton rbUberBlack=carType.findViewById(R.id.rbUberBlack);

        if(Common.currentDriver.getVehicle().equals("UberX"))
            rbUberX.setChecked(true);
        else if(Common.currentDriver.getVehicle().equals("Uber Black"))
            rbUberBlack.setChecked(true);

        alertDialog.setView(carType);
        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                final android.app.AlertDialog waitingDialog = new SpotsDialog(DriverHome.this);
                waitingDialog.show();
                Map<String, Object> updateInfo=new HashMap<>();
                if(rbUberX.isChecked())
                    updateInfo.put("carType", rbUberX.getText().toString());
                else if(rbUberBlack.isChecked())
                    updateInfo.put("carType", rbUberBlack.getText().toString());

                /*DatabaseReference driverInformation = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
                driverInformation.child(Common.userID)
                        .updateChildren(updateInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                waitingDialog.dismiss();
                                if(task.isSuccessful())
                                    Toast.makeText(DriverHome.this,"Information Updated!",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(DriverHome.this,"Information Update Failed!",Toast.LENGTH_SHORT).show();

                            }
                        });
                driverInformation.child(Common.userID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Common.currentUser=dataSnapshot.getValue(User.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showDialogUpdateInfo() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriverHome.this);
        alertDialog.setTitle("UPDATE INFORMATION");
        LayoutInflater inflater = this.getLayoutInflater();
        View layout_pwd = inflater.inflate(R.layout.layout_update_information, null);
        final MaterialEditText etName = (MaterialEditText) layout_pwd.findViewById(R.id.etName);
        final MaterialEditText etPhone = (MaterialEditText) layout_pwd.findViewById(R.id.etPhone);
        final ImageView image_upload = (ImageView) layout_pwd.findViewById(R.id.imageUpload);
        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        alertDialog.setView(layout_pwd);
        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                final android.app.AlertDialog waitingDialog = new SpotsDialog(DriverHome.this);
                waitingDialog.show();
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();

                Map<String, Object> updateInfo = new HashMap<>();
                if(!TextUtils.isEmpty(name))
                    updateInfo.put("name", name);
                if(!TextUtils.isEmpty(phone))
                    updateInfo.put("phone",phone);
                DatabaseReference driverInformation = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
                /*driverInformation.child(Common.userID)
                        .updateChildren(updateInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                waitingDialog.dismiss();
                                if(task.isSuccessful())
                                    Toast.makeText(DriverHome.this,"Information Updated!",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(DriverHome.this,"Information Update Failed!",Toast.LENGTH_SHORT).show();

                            }
                        });*/
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void chooseImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent=new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, Common.PICK_IMAGE_REQUEST);
                        }else{
                            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri saveUri=data.getData();
            if(saveUri!=null){
                final ProgressDialog progressDialog=new ProgressDialog(this);
                progressDialog.setMessage("Uploading...");
                progressDialog.show();

                String imageName=UUID.randomUUID().toString();
                final StorageReference imageFolder=storageReference.child("images/"+imageName);

                imageFolder.putFile(saveUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(DriverHome.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                        imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                
                                Map<String, Object> avatarUpdate=new HashMap<>();
                                avatarUpdate.put("avatarUrl", uri.toString());

                                /*DatabaseReference driverInformations=FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
                                driverInformations.child(Common.userID).updateChildren(avatarUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(DriverHome.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(DriverHome.this, "Uploaded error!", Toast.LENGTH_SHORT).show();

                                    }
                                });*/
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded "+progress+"%");
                    }
                });
            }
        }
    }

    private void showDialogChangePwd() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriverHome.this);
        alertDialog.setTitle("CHANGE PASSWORD");


        LayoutInflater inflater = this.getLayoutInflater();
        View layout_pwd = inflater.inflate(R.layout.layout_change_pwd, null);

        final MaterialEditText edtPassword = (MaterialEditText) layout_pwd.findViewById(R.id.edtPassword);
        final MaterialEditText edtNewPassword = (MaterialEditText) layout_pwd.findViewById(R.id.edtNewPassword);
        final MaterialEditText edtRepeatPassword = (MaterialEditText) layout_pwd.findViewById(R.id.edtRepetPassword);

        alertDialog.setView(layout_pwd);

        alertDialog.setPositiveButton("CHANGE PASSWORD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final android.app.AlertDialog waitingDialog = new SpotsDialog(DriverHome.this);
                waitingDialog.show();

                if (edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString())) {
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    //Get auth credentials from the user for re-authentication.
                    //Example with only email
                    AuthCredential credential = EmailAuthProvider.getCredential(email, edtPassword.getText().toString());
                    FirebaseAuth.getInstance().getCurrentUser()
                            .reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseAuth.getInstance().getCurrentUser()
                                                .updatePassword(edtRepeatPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {
                                                            //update driver information password column
                                                            Map<String, Object> password = new HashMap<>();
                                                            password.put("password", edtRepeatPassword.getText().toString());
                                                            /*DatabaseReference driverInformation = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
                                                            driverInformation.child(Common.userID)
                                                                    .updateChildren(password)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful())
                                                                                Toast.makeText(DriverHome.this, "Password was changed!", Toast.LENGTH_SHORT).show();
                                                                            else
                                                                                Toast.makeText(DriverHome.this, "Password was doesn't changed!", Toast.LENGTH_SHORT).show();
                                                                            waitingDialog.dismiss();

                                                                        }
                                                                    });*/

                                                        } else {
                                                            Toast.makeText(DriverHome.this, "Password doesn't change", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });

                                    } else {
                                        waitingDialog.dismiss();
                                        Toast.makeText(DriverHome.this, "Wrong old password", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                } else {
                    waitingDialog.dismiss();
                    Toast.makeText(DriverHome.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                }


            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        //show dialog
        alertDialog.show();

    }

    private void signOut() {
        if(account!=null) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        Intent intent = new Intent(DriverHome.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DriverHome.this, "Could not log out", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private class PlaceItems{
        public PlaceItems(String id, String name) {
            Id = id;
            this.name = name;
        }

        String Id, name;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}