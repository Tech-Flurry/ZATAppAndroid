package com.sparkers.zatappdriver.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sparkers.zatappdriver.R;

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar splashProgressBar;
    TextView txtErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashProgressBar = findViewById(R.id.splashProgressBar);
        txtErrorText = findViewById(R.id.txtErrorNoInternet);
        isInternetAvailable();
    }

    private void isInternetAvailable() {
        //checking for the internet
        RequestQueue queue = Volley.newRequestQueue(SplashScreenActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://www.google.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Intent i;
                    SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.ZAT_Storage_Preferences), MODE_PRIVATE);
                    if (preferences.getBoolean("loggedInFlag", false)) {
                        i = new Intent(SplashScreenActivity.this, DriverHome.class);
                    } else {
                        i = new Intent(SplashScreenActivity.this, Login.class);
                    }
                    startActivity(i);
                    //Let's Finish Splash Activity since we don't want to show this when user press back button.
                    finish();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                splashProgressBar.setVisibility(View.INVISIBLE);
                txtErrorText.setVisibility(View.VISIBLE);
            }
        });
        queue.add(request);
    }
}
