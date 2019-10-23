package com.sparkers.zatappdriver.Helpers;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sparkers.zatappdriver.Activities.DriverHome;
import com.sparkers.zatappdriver.Common.Common;
import com.sparkers.zatappdriver.R;

import java.util.Queue;

import static android.content.Context.MODE_PRIVATE;

public class SignInDialog extends Dialog implements View.OnClickListener {
    private EditText edtUsername, edtPassword;
    private Button btnSignIn;
    private LoadingDialog loadingDialog;
    private RequestQueue queue;

    public SignInDialog(@NonNull final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_login);
        setCancelable(true);
        loadingDialog = new LoadingDialog(context);
        queue = Volley.newRequestQueue(context);
        edtUsername = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String requestUrl = Common.ZAT_API_HOST + "Drivers/MatchCredentials/" + username + "/" + password;
                StringRequest request = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long result = Long.parseLong(response);
                        if (result > 0) {
                            SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.ZAT_Storage_Preferences), MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("loggedInFlag", true);
                            editor.putLong("userId", result);
                            editor.apply();
                            Intent i = new Intent(context, DriverHome.class);
                            dismiss();
                            loadingDialog.dismiss();
                            context.startActivity(i);
                        } else {
                            loadingDialog.dismiss();
                            dismiss();
                            Toast.makeText(context, "Sign-In Error! Username or Password may be incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismiss();
                        dismiss();
                    }
                });
                queue.add(request);
                loadingDialog.show();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
