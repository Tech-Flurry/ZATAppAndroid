package com.sparkers.zatappdriver.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.sparkers.zatappdriver.R;

public class CancelingRideDialog extends Dialog implements View.OnClickListener {
    public CancelingRideDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.canceling_ride_dialog_layout);
        setCancelable(false);
    }

    @Override
    public void onClick(View view) {

    }
}
