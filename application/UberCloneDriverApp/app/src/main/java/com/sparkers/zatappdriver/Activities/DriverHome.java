package com.sparkers.zatappdriver.Activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import com.sparkers.zatappdriver.Helpers.CancelingRideDialog;
import com.sparkers.zatappdriver.Helpers.LoadingDialog;
import com.sparkers.zatappdriver.Helpers.PaymentDetailsDialog;
import com.sparkers.zatappdriver.Helpers.TransferingRideDialog;
import com.sparkers.zatappdriver.Interfaces.locationListener;
import com.sparkers.zatappdriver.Messages.Errors;
import com.sparkers.zatappdriver.Messages.Message;
import com.sparkers.zatappdriver.Model.Driver;
import com.sparkers.zatappdriver.Model.PaymentDetails;
import com.sparkers.zatappdriver.Model.Ride;
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
    private boolean rideFlag=false;
    private boolean pickUpFlag=false;

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
    private CardView cardDestinationInfo;
    private CardView cardRiderInfo;
    private CardView cardRideButtons;
    private ImageButton btnTransfer, btnCancelRide, btnPickUp;
    private TextView txtDistance;
    private TextView txtDuration;
    private TextView txtRiderName;
    private ImageButton btnCallRider;
    private Button btnEndRide;
    private NotificationManager notificationManager;
    private LoadingDialog loadingDialog;
    private CancelingRideDialog cancelingRideDialog;
    private TransferingRideDialog transferingRideDialog;
    StorageReference storageReference;


    private float getBearing(LatLng startPosition, LatLng endPosition) {
        //Method to get rotation angle for a marker
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //keeps the screen open
        setContentView(R.layout.activity_drawer_home);
        loadingDialog= new LoadingDialog(DriverHome.this); //initializing loading dialog to be show while the application is calling an API
        cardDestinationInfo=findViewById(R.id.cardDestinationDetails); //card view which shows the destination information
        cardRiderInfo=findViewById(R.id.cardViewRiderInfo); //card view which shows the rider information
        cardRideButtons=findViewById(R.id.cardRideButtons); //card view which contains the buttons i.e. cancel, end and pickup ride
        btnCancelRide =findViewById(R.id.btnCancelRide); //button on the card view to cancel a ride
        btnCancelRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code for Cancel Ride
                String requestUrl=Common.ZAT_API_HOST+"Rides/"+Common.currentRide.getId()+"/CancelRide"; //requests the API to cancel ride
                StringRequest request = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endRide();
                        cancelingRideDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("LocationUpdate",error.getMessage());
                    }
                });
                queue.add(request); //start request
                //shows a waiting dialog
                cancelingRideDialog = new CancelingRideDialog(DriverHome.this);
                cancelingRideDialog.show();
            }
        });
        btnTransfer=findViewById(R.id.btnTransferRide); //button on the card to transfer the ride to another available driver
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code to transfer ride
                String requestUrl=Common.ZAT_API_HOST+"Rides/"+Common.currentRide.getId()+"/TransferRide"; //requests the API to transfer ride
                StringRequest request = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean flag= Boolean.parseBoolean(response); //API response is : True->Ride transferred, False->No Driver Available
                        if (flag){
                            //if true then end the ride from this driver
                            endRide();
                            transferingRideDialog.dismiss(); //vanish the waiting dialog
                        }
                        else {
                            //if the ride is not transferred then do nothing just remove the waiting dialog
                            transferingRideDialog.dismiss();
                            Toast.makeText(DriverHome.this, "No other driver is available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("LocationUpdate",error.getMessage());
                    }
                });
                queue.add(request); //start request
                //show a waiting dialog to transfer ride
                transferingRideDialog= new TransferingRideDialog(DriverHome.this);
                transferingRideDialog.show();
            }
        });
        btnPickUp=findViewById(R.id.btnPickUp); //button on the card view to pickup the ride
        btnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code to pick-up ride
                String requestUrl=Common.ZAT_API_HOST+"Drivers/"+Common.userID+"/PickUpRide/"+Common.currentRide.getId(); //requests the API to Pick Up the ride with ride Id
                //this is a post request and need a JSON Object for pick-up location
                JSONObject requestBody= new JSONObject();
                try {
                    requestBody.put("Latitude",Common.currentLat);
                    requestBody.put("Longitude", Common.currentLng);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestUrl, requestBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Common.currentRide= new Ride(response); //updates the ride info
                            pickUpFlag=true; //this flag shows that if the ride is picked-up or not, by setting it to true means that ride is picked up
                            cardRideButtons.setVisibility(View.INVISIBLE); //hide the card view for ride buttons because there is no need of them more
                            loadingDialog.dismiss(); //vanish the loading dialog
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(request); //starts the request
                    loadingDialog.show(); //displays the loading dialog
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        txtRiderName=findViewById(R.id.txtRiderName); //Text View on the rider-info card view to show the rider name
        btnCallRider=findViewById(R.id.btnCallRider); //button on the card view to call the current rider
        btnCallRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the rider
                String riderNumber=Common.currentRide.getRider().getPhone(); //gets the current rider number
                //checking call permission on the runtime
                int permissionCheck = ContextCompat.checkSelfPermission(DriverHome.this, Manifest.permission.CALL_PHONE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            DriverHome.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            123);
                } else {
                    startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+riderNumber)));
                }
            }
        });
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE); //generates the notifications for the driver
        txtDistance=findViewById(R.id.txtDistance); //text view to show the distance remaining to the destination
        txtDuration=findViewById(R.id.txtDuration); //text view to show the time remaining to reach the destination
        btnEndRide=findViewById(R.id.btnEndRide); //button to end the ride after reaching the the destination (will work only if the rider is picked up by the driver)
        btnEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickUpFlag){
                    //if the ride is picked up by the driver
                    //code to end ride
                    //request the API to end the ride
                    String requestUrl=Common.ZAT_API_HOST+"Rides/"+Common.currentRide.getId()+"/EndRide?Latitude="+Common.currentLat+"&Longitude="+Common.currentLng;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestUrl, "", new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            loadingDialog.dismiss(); //vanish the waiting dialog
                            getPaymentDetails(response); //sending the response to other method to get payment details about this ride
                            endRide(); //resets the UI
                            loadUser(); //refreshes the driver's details

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.e("ActiveRide",error.getMessage());
                        }
                    });
                    queue.add(request); //start the request
                    loadingDialog.show(); //shows the waiting dialog
                }
                else {
                    //if the ride is not picked up, the driver has other options, i.e. transfer or cancel the ride, at this point
                    Toast.makeText(DriverHome.this,"You cannot end a ride before pick-up",Toast.LENGTH_SHORT).show();
                }

            }
        });
        queue=Volley.newRequestQueue(DriverHome.this); //initiating the Volley Request Queue
        verifyUserAccount();
        toolbar = findViewById(R.id.toolbar); //custom made toolbar
        setSupportActionBar(toolbar); //set custom toolbar as the action bar

        //initializing location variable to do constant location updates
        location=new Location(this, new locationListener() {
            @Override
            public void locationResponse(LocationResult response) {
                //method of the anonymous class to get time to time location updates

                //setting up latest location
                Common.currentLat=response.getLastLocation().getLatitude();
                Common.currentLng=response.getLastLocation().getLongitude();

                updateLocation();
                getActiveRide();
                setActiveStatus(true);

                if (destination!=null){
                    //there is a destination set then there must be a ride, so transform the UI for a ride

                    //remove old things on the map
                    if(currentLocationMarker!=null)
                        currentLocationMarker.remove();
                    if(blackPolyline!=null)
                        blackPolyline.remove();
                    if (greyPolyline!=null)
                        greyPolyline.remove();
                    //gets a newer direction according to the new current location
                    getDirection();
                }
                else{
                    //but if there is no destination set then just stay on the position and clear the map and display the current location
                    mMap.clear();
                    displayLocation();
                }

                if (Common.currentRide!=null && pickUpFlag){
                    //if there is a ride assigned to the driver and that ride is picked up then add the rout details to the server
                    updateRoute();
                }
            }
        });
        locationSwitch=findViewById(R.id.locationSwitch); //switch to get active and only an active driver can get rides
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //if the driver selects to be online then the application starts location updates
                    location.initializeLocation();
                    btnMyLocation.setVisibility(View.VISIBLE); //button for showing current location marker will be set to visible
                }
                else{
                    //if the driver sets itself offline
                    if (Common.currentRide==null){
                        //the driver can only be offline if there's no ride assigned to it
                        location.stopUpdateLocation(); //stops the location updates
                        currentLocationMarker.remove(); //removes the current location marker
                        mMap.clear(); //clears the map
                        setActiveStatus(false); //set active status to inactive
                        btnMyLocation.setVisibility(View.INVISIBLE); //hide the my location button
                        cardDestinationInfo.setVisibility(View.INVISIBLE); //hide the destination info card view
                    }
                    else{
                        //if the driver has a ride assigned then the driver can't get offline and the location switch will remain true
                        Toast.makeText(DriverHome.this, "You can't get Offline during a ride", Toast.LENGTH_SHORT).show();
                        locationSwitch.setChecked(true);
                    }
                }
            }
        });
        btnMyLocation=findViewById(R.id.btnMyLocation); //button which will show the current location
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerFlag=false; //sets the marker flag t false so that it will come back to the current point
            }
        });
        polyLineList=new ArrayList<>(); //contains the list of points through which the polyline will be made
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpLocation();
    }

    private void endRide() {
        //Method to refresh the map screen to initial
        cardRideButtons.setVisibility(View.INVISIBLE);
        cardRiderInfo.setVisibility(View.INVISIBLE);
        Common.currentRide=null;
        destination=null;
        carMarker.remove();
        carMarker=null;
        blackPolyline.remove();
        greyPolyline.remove();
        TransitionManager.beginDelayedTransition(cardDestinationInfo);
        cardDestinationInfo.setVisibility(View.INVISIBLE);
        pickUpFlag=false;
        rideFlag=false;
        mMap.clear();
    }

    private void getPaymentDetails(JSONObject response) {
        //shows the payment details of the ended ride
        PaymentDetails paymentDetails= new PaymentDetails(response);
        PaymentDetailsDialog paymentDetailsDialog= new PaymentDetailsDialog(DriverHome.this,paymentDetails, Common.currentRide.getId());
        paymentDetailsDialog.show();
    }

    private void updateRoute() {
        //Updates the Route for ride
        String requestUrl=Common.ZAT_API_HOST+"Rides/"+Common.currentRide.getId()+"/AddRoute?Latitude="+Common.currentLat+"&Longitude="+Common.currentLng;
        StringRequest request = new StringRequest(Request.Method.GET, requestUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(DriverHome.this, "Route Added", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("UpdateRoute",error.getMessage());
            }
        });
        queue.add(request);
    }

    private void getActiveRide() {
        //Gets the active ride for the driver from the server
        String requestUrl=Common.ZAT_API_HOST+"Drivers/"+Common.userID+"/GetActiveRide";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestUrl, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Ride ride= new Ride(response);
                Common.currentRide=ride;
                if(pickUpFlag){
                    destination=ride.getDestination(); //if the ride is picked up then the maker will show the destination
                }
                else {
                    //if the ride is not picked up then the marker will show the pickup point
                    destination=ride.getPickUpLocation();
                }

                if(!rideFlag){
                    //if the ride is fetched for the fist time through the server then it will create a notification
                    NotificationCompat.Builder rideNotification= new NotificationCompat.Builder(getBaseContext())
                            .setSmallIcon(R.drawable.ic_location)
                            .setContentTitle("New Ride")
                            .setContentText("You have a new Ride to pick-up")
                            .setPriority(NotificationCompat.PRIORITY_HIGH);
                    rideNotification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                    notificationManager.notify(101, rideNotification.build());
                    rideFlag=true;
                    cardRiderInfo.setVisibility(View.VISIBLE);
                    txtRiderName.setText(Common.currentRide.getRider().getFullName());
                }
                //ride buttons card view will become invisible if the ride is picked up
                if (!pickUpFlag){
                    cardRideButtons.setVisibility(View.VISIBLE);
                }
                else{
                    cardRideButtons.setVisibility(View.INVISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("ActiveRide",error.getMessage());
            }
        });
        queue.add(request);
    }

    private void updateLocation() {
        //updates the latest location to the server
        String locationUrl=Common.ZAT_API_HOST+"Drivers/"+Common.userID+"/UpdateLocation?Latitude="+Common.currentLat+"&Longitude="+Common.currentLng;
        StringRequest request = new StringRequest(Request.Method.GET, locationUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("LocationUpdate",error.getMessage());
            }
        });
        queue.add(request);
    }

    private void setActiveStatus(boolean status) {
        //sets the active status on the server
        String locationUrl=Common.ZAT_API_HOST+"Drivers/"+Common.userID+"/ChangeActiveStatus/"+status;
        StringRequest request= new StringRequest(Request.Method.GET, locationUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("ActiveStatus",error.getMessage());
            }
        });
        queue.add(request);
    }

    public void initDrawer(){
        //initialize the navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView=navigationView.getHeaderView(0);
        TextView tvName= navigationHeaderView.findViewById(R.id.tvDriverName);
        RatingBar ratingStars= navigationHeaderView.findViewById(R.id.ratingStars);
        CircleImageView imageAvatar= navigationHeaderView.findViewById(R.id.imageAvatar);
        TextView txtBalance=navigationHeaderView.findViewById(R.id.txtBalance);
        tvName.setText(Common.currentDriver.getName());
        txtBalance.setText(Double.toString(Common.currentDriver.getBalance()));
        if(Common.currentDriver.getRating()!=0.0f &&
                !TextUtils.isEmpty(Common.currentDriver.getRating()+""))
            ratingStars.setRating(Common.currentDriver.getRating());

         /*if(isLoggedInFacebook)
            Picasso.get().load("https://graph.facebook.com/" + Common.userID + "/picture?width=500&height=500").into(imageAvatar);
        else if(account!=null)
            Picasso.get().load(account.getPhotoUrl()).into(imageAvatar);*/

        if(Common.currentDriver.getAvatarUrl()!=null &&
                !TextUtils.isEmpty(Common.currentDriver.getAvatarUrl()))
        Picasso.get().load(Common.currentDriver.getAvatarUrl()).into(imageAvatar);
    }

    private void loadUser(){
        //loads the user details from the server
        loadingDialog.show();
        String requestUrl=Common.ZAT_API_HOST+"drivers/"+Common.userID;
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, requestUrl, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Common.currentDriver = new Driver(response);
                getUserVehicle();
                initDrawer();
                loadingDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                 //Log.e("loadUser",error.getMessage());
            }
        });
        queue.add(request);
    }

    private void getUserVehicle() {
        //gets the information about the user vehicle
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
        //verifies the user account whether it is authenticated or not
        Common.userID=48;
        loadUser();
    }

    private void getDirection(){
        //draws a route on the map towards the destination
        currentPosition=new LatLng(Common.currentLat, Common.currentLng); //current position of the driver
        final String requestApi;
        //google api to get the direction
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
                        cardDestinationInfo.setVisibility(View.VISIBLE); //makes the ride details card view visible, showing the information like distance and time
                        String encodedPolyline = route.getJSONObject("overview_polyline").getString("points"); //gets the points of the route by decoding the overview polyline response from the google API
                        polyLineList=decodePoly(encodedPolyline);
                        if (!polyLineList.isEmpty()) {
                            //creates poly line on the map
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
                            if(pickUpFlag){
                                //if the ride is picked up then the new marker will be called as the destination
                                mMap.addMarker(new MarkerOptions().position(polyLineList.get(polyLineList.size() - 1))
                                        .title("Destination"));
                            }
                            else{
                                mMap.addMarker(new MarkerOptions().position(polyLineList.get(polyLineList.size() - 1))
                                        .title("Pickup location"));
                            }


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
                                    LatLng newPosition;
                                    try{
                                        newPosition= polyLineList.get(1);
                                    }
                                    catch (IndexOutOfBoundsException ex){
                                        newPosition=polyLineList.get(0);
                                    }

                                    blackPolyline.setPoints(p);
                                    if(carMarker!=null){
                                        carMarker.setVisible(true);
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
        //decodes the polyline

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
        //checks the location permission on the runtime
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
        //displays the current location
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
        mMap.getUiSettings().setCompassEnabled(true);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
            case R.id.nav_clear_account:
                showDialogUpdateCarType();
                break;
            case R.id.nav_balance_details:
                //showDialogUpdateInfo();
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