package com.fr.marcoucou.placereminder.fragment;


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
import android.widget.Toast;

import com.fr.marcoucou.placereminder.DBLite.PlacesDataSource;
import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.activities.MapActivity;
import com.fr.marcoucou.placereminder.activities.PlaceInformation;
import com.fr.marcoucou.placereminder.activities.PlaceListAll;
import com.fr.marcoucou.placereminder.adapter.ListPlacesAdapter;
import com.fr.marcoucou.placereminder.adapter.ViewPagerAdapter;
import com.fr.marcoucou.placereminder.animation.MyPagerTransformer;
import com.fr.marcoucou.placereminder.model.Places;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesViewPagerFragment extends Fragment {

    private static final String ARG_PARAM1 = "position";
    private ViewPager mPager;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabMap;
    private FloatingActionButton fabViewAll;
    private PlacesDataSource placesDataSource;
    private int mParam1;
    private ViewPagerAdapter viewPagerAdapter;
    private Context context;
    private MyPagerTransformer pagerTransformer;


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
        context = container.getContext();
        initializationView(myFragmentView);
        return myFragmentView;
    }



    public void initializationView(View myFragmentView){
        placesDataSource = new PlacesDataSource(context);
        placesDataSource.open();
        final ArrayList<Places> itemPlaces;

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            if (mParam1 != 0){

                itemPlaces = placesDataSource.getPlacesFromCategory(mParam1);
                viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(),context, itemPlaces);
                mPager.setAdapter(viewPagerAdapter);
                mPager.setPageTransformer(false,new MyPagerTransformer(MyPagerTransformer.TypeAnimation.FLOW));
            }else
            {
                itemPlaces = placesDataSource.getAllPlaces();
                viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(),context, itemPlaces);
                mPager.setAdapter(viewPagerAdapter);
                mPager.setPageTransformer(false,new MyPagerTransformer(MyPagerTransformer.TypeAnimation.FLOW));
            }


            fabAdd = (FloatingActionButton) myFragmentView.findViewById(R.id.fabAdd);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlaceInformation.class);
                    intent.putExtra("indexPlace",getArguments().getInt(ARG_PARAM1));
                    startActivity(intent);
                }
            });
            fabMap = (FloatingActionButton) myFragmentView.findViewById(R.id.fabMap);
            fabMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemPlaces.size() > 0) {
                        Intent intent = new Intent(getActivity(), MapActivity.class);
                        intent.putExtra("address",itemPlaces.get(mPager.getCurrentItem()).getAdresse());
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(context, "No places found in this category", Toast.LENGTH_LONG).show();
                    }

                }
            });
            fabViewAll = (FloatingActionButton) myFragmentView.findViewById(R.id.fabViewAll);
            fabViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlaceListAll.class);
                    intent.putExtra("indexPlace",getArguments().getInt(ARG_PARAM1));
                    startActivity(intent);
                }
            });

        }
        placesDataSource.close();
    }

}
