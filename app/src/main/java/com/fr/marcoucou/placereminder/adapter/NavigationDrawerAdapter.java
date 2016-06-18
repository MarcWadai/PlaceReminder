package com.fr.marcoucou.placereminder.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.model.NavigationDrawer;
import com.fr.marcoucou.placereminder.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Marc on 11/03/2016.
 */

public class NavigationDrawerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavigationDrawer> navDrawerItems;

    public NavigationDrawerAdapter(Context context, ArrayList<NavigationDrawer> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
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
            convertView = mInflater.inflate(R.layout.navdrawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), Constants.TYPEFACE_NAME);
        txtTitle.setTypeface(typeface);
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        animationIcon(imgIcon);

        return convertView;
    }


    public void animationIcon(ImageView icon){
        RotateAnimation anim = new RotateAnimation(10f, -10f,0f, 0f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(300);

        icon.startAnimation(anim);

    }

}
