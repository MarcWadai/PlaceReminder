package com.fr.marcoucou.placereminder;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fr.marcoucou.placereminder.adapter.ListPlacesAdapter;

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
    private ArrayList<String> itemString;
    private Context context;
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
        context = container.getContext();
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            itemString = new ArrayList<String>();
            itemString.add("hello");
            itemString.add("bonjour");
            itemString.add("nihao");

            adapter = new ListPlacesAdapter(context,
                    itemString);

            listPlaces.setAdapter(adapter);
            Log.d("param","In the fragment " + mParam1);
        }
        return myFragmentView;
    }



}
