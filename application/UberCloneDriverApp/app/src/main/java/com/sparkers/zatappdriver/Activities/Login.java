package com.sparkers.zatappdriver.Activities;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sparkers.zatappdriver.Helpers.SignInDialog;
import com.sparkers.zatappdriver.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Login extends AppCompatActivity {
    Button btnSignIn;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/NotoSans.ttf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_login);
        btnSignIn=findViewById(R.id.btnSignin);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInDialog signInDialog = new SignInDialog(Login.this);
                signInDialog.show();
            }
        });
    }

}