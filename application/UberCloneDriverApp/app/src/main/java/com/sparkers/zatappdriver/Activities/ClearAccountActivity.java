package com.sparkers.zatappdriver.Activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sparkers.zatappdriver.Common.Common;
import com.sparkers.zatappdriver.Helpers.LoadingDialog;
import com.sparkers.zatappdriver.R;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Queue;

public class ClearAccountActivity extends AppCompatActivity {

    private Button btnSubmitClearAccount;
    private EditText edtRefNumber, edtAmount;
    private Spinner spinAccountType;
    private TextView txtErrorRequired;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_account);
        initToolbar();
        Common.userID = getIntent().getLongExtra("DriverId", 0);
        loadingDialog = new LoadingDialog(ClearAccountActivity.this);
        final String[] paymentTypes = getResources().getStringArray(R.array.payment_options);
        txtErrorRequired = findViewById(R.id.txtRequiredError);
        edtRefNumber = findViewById(R.id.edtRefNumber);
        edtAmount = findViewById(R.id.edtAmount);
        spinAccountType = findViewById(R.id.spinAccountType);

        btnSubmitClearAccount = findViewById(R.id.btnSubmitClearAccount);
        btnSubmitClearAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtAmount.getText().length() < 1 || edtRefNumber.getText().length() < 1) {
                    txtErrorRequired.setVisibility(View.VISIBLE);
                } else {
                    RequestQueue queue = Volley.newRequestQueue(ClearAccountActivity.this);
                    String requestUrl = Common.ZAT_API_HOST + "Drivers/" + Common.userID + "/MakeMobileAccountTransaction/" + edtRefNumber.getText() + "/" + spinAccountType.getSelectedItem() + "/" + edtAmount.getText();
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestUrl, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            loadingDialog.dismiss();
                            Toast.makeText(ClearAccountActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                            edtAmount.getText().clear();
                            edtRefNumber.getText().clear();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loadingDialog.dismiss();
                            Toast.makeText(ClearAccountActivity.this, "Error. Make sure you are entering valid data.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(request);
                    loadingDialog.show();
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Clear Account");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
