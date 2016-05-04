package com.fr.marcoucou.placereminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fr.marcoucou.placereminder.DBLite.PlacesDataSource;
import com.fr.marcoucou.placereminder.activities.PlaceInformation;
import com.fr.marcoucou.placereminder.adapter.ListPlacesAdapter;
import com.fr.marcoucou.placereminder.model.Places;

import java.util.ArrayList;


public class ListPlacesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "position";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private ListView listPlaces;
    private ListPlacesAdapter adapter;
    private ArrayList<Places> itemPlaces;
    private Context context;
    private PlacesDataSource placesDataSource;
    private FloatingActionButton fabAddPlace;
    public ListPlacesFragment() {
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
        View myFragmentView = inflater.inflate(R.layout.fragment_list_places, container, false);
        listPlaces = (ListView) myFragmentView.findViewById(R.id.list_places);
        fabAddPlace = (FloatingActionButton) myFragmentView.findViewById(R.id.fab);
        fabAddPlace.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), PlaceInformation.class);
                startActivity(intent);
            }
        });
        context = container.getContext();
        //initialize the data source to geet and save the data
        initializationView();

        return myFragmentView;
    }


    public void initializationView(){
        placesDataSource = new PlacesDataSource(context);
        placesDataSource.open();

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            itemPlaces = placesDataSource.getPlacesFromCategory(getArguments().getInt("position"));
            adapter = new ListPlacesAdapter(context,
                    itemPlaces);
            listPlaces.setAdapter(adapter);
            Log.d("param","In the fragment " + mParam1);
        }
    }

}
