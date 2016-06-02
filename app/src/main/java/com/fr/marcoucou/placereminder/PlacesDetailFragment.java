package com.fr.marcoucou.placereminder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fr.marcoucou.placereminder.model.Places;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesDetailFragment extends Fragment {


    private Places place;
    public PlacesDetailFragment(){
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState){
        Bundle bundle = this.getArguments();
         place = bundle.getParcelable("place");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places_detail, container, false);
    }

}
