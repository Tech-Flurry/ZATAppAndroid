package com.sparkers.zatappdriver.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sparkers.zatappdriver.Model.History;
import com.sparkers.zatappdriver.R;

import java.util.ArrayList;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.ViewHolder>{
    private Context context;
    private ArrayList<History> items;
    private ClickListener listener;

    public historyAdapter(Context context, ArrayList<History> items, ClickListener listener ){
        this.context=context;
        this.items=items;
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.history_template,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvRiderName.setText("Rider Name: "+items.get(i).getName());
        viewHolder.tvTripDate.setText("Date: "+items.get(i).getTripDate());
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvRiderName, tvTripDate;
        ClickListener listener;
        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            tvRiderName=itemView.findViewById(R.id.tvRiderName);
            tvTripDate=itemView.findViewById(R.id.tvTripDate);
            this.listener=listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.listener.onClick(view, getAdapterPosition());
        }
    }
}
