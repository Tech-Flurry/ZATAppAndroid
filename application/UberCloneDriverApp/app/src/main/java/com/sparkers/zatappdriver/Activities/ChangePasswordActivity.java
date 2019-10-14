package com.sparkers.zatappdriver.Activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sparkers.zatappdriver.Common.Common;
import com.sparkers.zatappdriver.Helpers.LoadingDialog;
import com.sparkers.zatappdriver.R;

import retrofit2.http.GET;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView txtErrorAllRequired, txtErrorPasswordsNontMatch;
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnSubmitChangePassword;
    private LoadingDialog loadingDialog;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initToolbar();
        Common.userID = getIntent().getLongExtra("DriverId", 0);
        txtErrorAllRequired = findViewById(R.id.txtErrorAllRequired);
        txtErrorPasswordsNontMatch = findViewById(R.id.txtErrorPasswordsNotMatched);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        btnSubmitChangePassword = findViewById(R.id.btnSubmitChangePassword);
        btnSubmitChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = edtOldPassword.getText().toString();
                String newPassword = edtNewPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();
                txtErrorAllRequired.setVisibility(View.INVISIBLE);
                txtErrorPasswordsNontMatch.setVisibility(View.INVISIBLE);
                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    txtErrorAllRequired.setVisibility(View.VISIBLE);
                } else if (!confirmPassword.equals(newPassword)) {
                    txtErrorPasswordsNontMatch.setVisibility(View.VISIBLE);
                } else {
                    String requestUrl = Common.ZAT_API_HOST + "drivers/" + Common.userID + "/ChangePassword/" + oldPassword + "/" + newPassword;
                    StringRequest request = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loadingDialog.dismiss();
                            Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            edtConfirmPassword.getText().clear();
                            edtNewPassword.getText().clear();
                            edtOldPassword.getText().clear();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loadingDialog.dismiss();
                            Toast.makeText(ChangePasswordActivity.this, "Password haven't been changed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(request);
                    loadingDialog.show();
                }
            }
        });
        loadingDialog = new LoadingDialog(ChangePasswordActivity.this);
        queue = Volley.newRequestQueue(ChangePasswordActivity.this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Change Password");

        ActionBar actionBar = getSupportActionBar();
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
