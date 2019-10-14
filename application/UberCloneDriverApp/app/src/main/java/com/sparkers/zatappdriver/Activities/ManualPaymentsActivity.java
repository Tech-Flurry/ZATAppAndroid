package com.sparkers.zatappdriver.Activities;

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
import com.android.volley.toolbox.Volley;
import com.sparkers.zatappdriver.Common.Common;
import com.sparkers.zatappdriver.Helpers.LoadingDialog;
import com.sparkers.zatappdriver.Model.ManualPayment;
import com.sparkers.zatappdriver.R;
import com.sparkers.zatappdriver.RecyclerView.ClickListener;
import com.sparkers.zatappdriver.RecyclerView.ManualTransactionAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ManualPaymentsActivity extends AppCompatActivity {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView rvMobilePayments;
    ManualTransactionAdapter adapter;
    ArrayList<ManualPayment> lstData;
    LoadingDialog loadingDialog;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_payments);
        Common.userID = getIntent().getLongExtra("DriverId", 0);
        loadingDialog = new LoadingDialog(ManualPaymentsActivity.this);
        queue = Volley.newRequestQueue(ManualPaymentsActivity.this);
        initToolbar();
        initRecyclerView();
        lstData = new ArrayList<>();
        adapter = new ManualTransactionAdapter(this, lstData, new ClickListener() {
            @Override
            public void onClick(View view, int index) {

            }
        });
        rvMobilePayments.setAdapter(adapter);
        getData();
    }

    private void getData() {
        String requestUrl = Common.ZAT_API_HOST + "drivers/" + Common.userID + "/GetAllManualTransactions";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, requestUrl, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        ManualPayment manualPayment = new ManualPayment(response.getJSONObject(i));
                        lstData.add(manualPayment);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ManualTransactions", error.getMessage());
            }
        });
        queue.add(request);
        loadingDialog.show();
    }

    private void initRecyclerView() {
        rvMobilePayments = findViewById(R.id.rvManualPayments);
        rvMobilePayments.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvMobilePayments.setLayoutManager(layoutManager);
        rvMobilePayments.setItemAnimator(new DefaultItemAnimator());
        rvMobilePayments.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Manual Payments");

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
