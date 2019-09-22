package com.sparkers.zatappdriver.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sparkers.zatappdriver.R;

import java.util.ArrayList;

public class AutoCompleteAdapter extends ArrayAdapter<PlacesAutoCompleteModel> {
    ArrayList<PlacesAutoCompleteModel> lstPlaces;
    Activity context;

    public AutoCompleteAdapter(ArrayList<PlacesAutoCompleteModel> lstPlaces, Activity context) {
        super(context,R.layout.autocomplete_item,lstPlaces);
        this.lstPlaces = lstPlaces;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lstPlaces.size();
    }

    @Override
    public PlacesAutoCompleteModel getItem(int i) {
        return lstPlaces.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.autocomplete_item, null);
        }
        PlacesAutoCompleteModel place= lstPlaces.get(position);
        if (place != null) {
            TextView name =v.findViewById(R.id.txtPlaceName);
            TextView address=v.findViewById(R.id.txtAddress);
            if (name != null) {
                name.setText(place.getName());
            }
            if (address!=null)
                address.setText(place.getAddress());
        }
        v.setTag(place.getLocation());
        return v;
    }
}
