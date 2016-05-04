package com.fr.marcoucou.placereminder.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.model.NavigationDrawer;
import com.fr.marcoucou.placereminder.model.Places;

import java.util.ArrayList;

/**
 * Created by Marc on 15/03/2016.
 */
public class ListPlacesAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Places> items;

    public ListPlacesAdapter(Context context, ArrayList<Places> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.places_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.iconPlace);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.titlePlaces);
        TextView txtAddress = (TextView) convertView.findViewById(R.id.textAdresse);
        txtTitle.setText(items.get(position).getTitle());
        txtAddress.setText(items.get(position).getAdresse());

        return convertView;
    }
}
