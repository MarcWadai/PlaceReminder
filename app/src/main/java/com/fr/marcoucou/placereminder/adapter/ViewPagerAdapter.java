package com.fr.marcoucou.placereminder.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fr.marcoucou.placereminder.PlacesDetailFragment;
import com.fr.marcoucou.placereminder.model.Places;

import java.util.ArrayList;

/**
 * Created by SIRISAK on 02/06/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private ArrayList<Places> placesItem;
    public ViewPagerAdapter(FragmentManager fm, Context context, ArrayList<Places> placesItem) {
        super(fm);
        this.context = context;
        this.placesItem = placesItem;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new  PlacesDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("place",placesItem.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return placesItem.size();
    }
}
