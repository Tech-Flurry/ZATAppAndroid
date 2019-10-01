package com.sparkers.zatappdriver.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sparkers.zatappdriver.Common.Common;
import com.sparkers.zatappdriver.Model.PaymentDetails;
import com.sparkers.zatappdriver.R;

public class PaymentDetailsDialog extends Dialog implements View.OnClickListener {
    public PaymentDetailsDialog(@NonNull final Context context, PaymentDetails paymentDetails, final long rideId) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.payment_details_layout);
        setCancelable(false);
        Button btnReceivePayment = findViewById(R.id.btnReceivePayment);
        btnReceivePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        TextView txtTotalFare = findViewById(R.id.txtTotalFare);
        txtTotalFare.setText(Double.toString(Math.round(paymentDetails.getgTotal())));
    }

    @Override
    public void onClick(View view) {

    }
}
