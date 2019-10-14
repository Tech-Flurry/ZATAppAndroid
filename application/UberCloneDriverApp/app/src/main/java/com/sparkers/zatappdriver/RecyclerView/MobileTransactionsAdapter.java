package com.sparkers.zatappdriver.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sparkers.zatappdriver.Model.MobileTransaction;
import com.sparkers.zatappdriver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MobileTransactionsAdapter extends RecyclerView.Adapter<MobileTransactionsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MobileTransaction> items;
    private ClickListener listener;
    private int verifiedColor, unverifiedColor;

    public MobileTransactionsAdapter(Context context, ArrayList<MobileTransaction> items, ClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verifiedColor = context.getColor(R.color.verifiedGreen);
            unverifiedColor = context.getColor(R.color.unverifiedRed);
        } else {
            verifiedColor = 0;
            unverifiedColor = 0;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.mobile_payments_item, viewGroup, false);
        MobileTransactionsAdapter.ViewHolder viewHolder = new MobileTransactionsAdapter.ViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txtServiceProvider.setText(items.get(i).getServiceProviderName());
        boolean isVerified = items.get(i).isVerified();
        if (isVerified) {
            viewHolder.txtVerified.setText("Verified");
            viewHolder.txtVerified.setTextColor(verifiedColor);
        } else {
            viewHolder.txtVerified.setText("Unverified");
            viewHolder.txtVerified.setTextColor(unverifiedColor);
        }
        viewHolder.txtRefNumber.setText(String.format("Ref#: %s", items.get(i).getReferenceNumber()));
        DateFormat format = new SimpleDateFormat("dd MMM yyyy '-' hh:mm a");
        viewHolder.txtDate.setText(format.format(items.get(i).getTransactionDate()));
        viewHolder.txtAmount.setText(String.format("Rs. %.2f", items.get(i).getAmount()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDate, txtAmount, txtVerified, txtServiceProvider, txtRefNumber;
        ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtRefNumber = itemView.findViewById(R.id.txtRefNumber);
            txtVerified = itemView.findViewById(R.id.txtVerified);
            txtServiceProvider = itemView.findViewById(R.id.txtServiceProvider);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.listener.onClick(view, getAdapterPosition());
        }
    }
}
