package com.sparkers.zatappdriver.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sparkers.zatappdriver.Model.ManualPayment;
import com.sparkers.zatappdriver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ManualTransactionAdapter extends RecyclerView.Adapter<ManualTransactionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ManualPayment> items;
    private ClickListener listener;

    public ManualTransactionAdapter(Context context, ArrayList<ManualPayment> items, ClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.manual_payments_item, viewGroup, false);
        ManualTransactionAdapter.ViewHolder viewHolder = new ManualTransactionAdapter.ViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DateFormat format = new SimpleDateFormat("dd MMM yyyy '-' hh:mm a");
        viewHolder.txtDate.setText(format.format(items.get(i).getTransactionDate()));
        viewHolder.txtAmount.setText(String.format("Rs. %.2f", items.get(i).getAmount()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDate, txtAmount;
        ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.listener.onClick(view, getAdapterPosition());
        }
    }
}
