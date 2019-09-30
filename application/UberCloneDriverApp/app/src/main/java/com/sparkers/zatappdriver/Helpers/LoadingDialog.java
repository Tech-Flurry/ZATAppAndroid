package com.sparkers.zatappdriver.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.sparkers.zatappdriver.R;

public class LoadingDialog extends Dialog implements View.OnClickListener {
    public LoadingDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog_layout);
    }

    @Override
    public void onClick(View view) {

    }
}
