package com.fr.marcoucou.placereminder.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.model.Places;
import com.fr.marcoucou.placereminder.utils.Constants;
import com.fr.marcoucou.placereminder.utils.DbBitmapUtility;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Marc on 15/03/2016.
 */
public class ListPlacesAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Places> items;
    private View.OnTouchListener listener;

    public ListPlacesAdapter(Context context, ArrayList<Places> items, View.OnTouchListener listener){
        this.context = context;
        this.items = items;
        this.listener = listener;
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
    public boolean hasStableIds(){
        return true;
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
            convertView = mInflater.inflate(R.layout.places_list_item,parent, false);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.iconPlace);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.titlePlaces);
        TextView txtAddress = (TextView) convertView.findViewById(R.id.textAdresse);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), Constants.TYPEFACE_NAME);
        txtAddress.setTypeface(typeface);
        txtTitle.setTypeface(typeface);
        txtTitle.setText(items.get(position).getTitle());
        txtAddress.setText(items.get(position).getAdresse());
        if(items.get(position).getPlaceImage() != null) {
            Bitmap circularimage = DbBitmapUtility.getCroppedCircularBitmap(items.get(position).getPlaceImage());
            imgIcon.setImageBitmap(circularimage);
        }
        convertView.setOnTouchListener(listener);
        return convertView;
    }
}
