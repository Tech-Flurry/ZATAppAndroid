package com.sparkers.zatappdriver.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.IInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sparkers.zatappdriver.Activities.DriverHome;
import com.sparkers.zatappdriver.Activities.ManualPaymentsActivity;
import com.sparkers.zatappdriver.Activities.MobileTransactionsActivity;
import com.sparkers.zatappdriver.R;

public class SelectPaymentTypeDialog extends Dialog implements View.OnClickListener {
    private RadioButton radManual, radMobile;
    private long userId;

    public SelectPaymentTypeDialog(@NonNull final Context context, final long userId) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_payment_type_dialog);
        setCancelable(true);
        this.userId = userId;
        radManual = findViewById(R.id.radManualPayment);
        radMobile = findViewById(R.id.radMobileTransaction);
        Button btnViewData = findViewById(R.id.btnViewData);
        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radManual.isChecked()) {
                    Intent i = new Intent(context, ManualPaymentsActivity.class);
                    i.putExtra("DriverId", userId);
                    context.startActivity(i);
                    dismiss();
                }
                if (radMobile.isChecked()) {
                    Intent i = new Intent(context, MobileTransactionsActivity.class);
                    i.putExtra("DriverId", userId);
                    context.startActivity(i);
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
