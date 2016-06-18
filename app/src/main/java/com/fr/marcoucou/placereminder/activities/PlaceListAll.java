package com.fr.marcoucou.placereminder.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fr.marcoucou.placereminder.DBLite.PlacesDataSource;
import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.adapter.ListPlacesAdapter;
import com.fr.marcoucou.placereminder.model.Places;
import com.fr.marcoucou.placereminder.utils.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PlaceListAll extends AppCompatActivity {

    private ListView listPlaces;
    private FloatingActionButton fabAddPlace;
    private int indexPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list_all);
        indexPlace = getIntent().getIntExtra("indexPlace", 0);

        listPlaces = (ListView) findViewById(R.id.list_places);
        fabAddPlace = (FloatingActionButton) findViewById(R.id.fab);
        fabAddPlace.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PlaceInformation.class);
                startActivity(intent);
            }
        });
        initializationView();
    }

    public void setListViewOnClick(){
        listPlaces.setClickable(true);
        listPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Places place = (Places) listPlaces.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("address", place.getAdresse());
                startActivity(intent);
            }
        });
    }


    public void initializationView() {
        PlacesDataSource placesDataSource = new PlacesDataSource(getApplicationContext());
        placesDataSource.open();
        if (indexPlace != 0){
            ArrayList<Places> itemPlaces = placesDataSource.getPlacesFromCategory(indexPlace);
            ListPlacesAdapter adapter = new ListPlacesAdapter(getApplicationContext(),
                    itemPlaces);
            listPlaces.setAdapter(adapter);
        }else{
            ArrayList<Places> itemPlaces = placesDataSource.getAllPlaces();
            ListPlacesAdapter adapter = new ListPlacesAdapter(getApplicationContext(),
                    itemPlaces);
            listPlaces.setAdapter(adapter);
        }

        setListViewOnClick();
        placesDataSource.close();
        TextView emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        Typeface typeface = Typeface.createFromAsset( getAssets(), Constants.TYPEFACE_NAME);
        emptyTextView.setTypeface(typeface);
        listPlaces.setEmptyView(findViewById(R.id.emptyTextView));
    }
}
