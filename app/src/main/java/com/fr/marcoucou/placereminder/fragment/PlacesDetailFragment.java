package com.fr.marcoucou.placereminder.fragment;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fr.marcoucou.placereminder.R;
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
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        place = bundle.getParcelable("place");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_detail, container, false);
        TextView title = (TextView) view.findViewById(R.id.detailTitle);
        TextView adress = (TextView) view.findViewById(R.id.detailAdress);
        ImageView image = (ImageView) view.findViewById(R.id.imageView2);

        title.setText(place.getTitle());
        adress.setText(place.getAdresse());
        image.setImageBitmap(place.getPlaceImage());

        // Inflate the layout for this fragment
        return view ;
    }

}
