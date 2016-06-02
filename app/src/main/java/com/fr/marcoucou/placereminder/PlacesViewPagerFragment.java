package com.fr.marcoucou.placereminder;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fr.marcoucou.placereminder.DBLite.PlacesDataSource;
import com.fr.marcoucou.placereminder.activities.PlaceInformation;
import com.fr.marcoucou.placereminder.adapter.ListPlacesAdapter;
import com.fr.marcoucou.placereminder.adapter.ViewPagerAdapter;
import com.fr.marcoucou.placereminder.model.Places;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesViewPagerFragment extends Fragment {

    private static final String ARG_PARAM1 = "position";
    private ViewPager mPager;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabDelete;
    private FloatingActionButton fabViewAll;
    private PlacesDataSource placesDataSource;
    private int mParam1;
    private ViewPagerAdapter viewPagerAdapter;
    private Context context;

    public PlacesViewPagerFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_places_view_pager, container, false);
        mPager = (ViewPager) myFragmentView.findViewById(R.id.pager);
        fabAdd = (FloatingActionButton) myFragmentView.findViewById(R.id.fabAdd);
        fabDelete = (FloatingActionButton) myFragmentView.findViewById(R.id.fabDelete);
        fabViewAll = (FloatingActionButton) myFragmentView.findViewById(R.id.fabViewAll);
        context = container.getContext();

        return myFragmentView;
    }



    public void initializationView(){
        placesDataSource = new PlacesDataSource(context);
        placesDataSource.open();

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            ArrayList<Places> itemPlaces = placesDataSource.getPlacesFromCategory(getArguments().getInt("position"));
            viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(),context, itemPlaces);
            mPager.setAdapter(viewPagerAdapter);
            Log.d("param","In the fragment " + mParam1);
        }
    }

}
