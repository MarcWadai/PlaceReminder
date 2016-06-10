package com.fr.marcoucou.placereminder.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fr.marcoucou.placereminder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LatLng latLng = new LatLng(0.0,0.0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);
        Log.d("place",getIntent().getStringExtra("address"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            googleMap.setMyLocationEnabled(true);
            String adress = getIntent().getStringExtra("address");cd Doc
            Boolean adressResult = getLatLongFromAddress(adress);
            if (adressResult){
                googleMap.addMarker(new MarkerOptions().position(latLng).title(adress));
            }
            else {
                Toast.makeText(getApplicationContext(), "Places not found, sorry", Toast.LENGTH_LONG);
            }
        } catch(SecurityException e){
            Toast.makeText(getApplicationContext(),"Please enable geolocalisation permision",Toast.LENGTH_LONG);
            Log.e("Security", "Security exception " + e);
        }
    }
    private Boolean getLatLongFromAddress(String address)
    {
        Boolean gotLatLng = false;
        double lat= 0.0, lng= 0.0;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName(address , 1);
            if (addresses.size() > 0)
            {
                Barcode.GeoPoint p = new Barcode.GeoPoint(2,
                        (addresses.get(0).getLatitude() * 1E6),
                         (addresses.get(0).getLongitude()) * 1E6);

                latLng = new LatLng(p.lat/1E6,p.lng/1E6) ;
                gotLatLng = true;
            }
            else{
                gotLatLng = false;
            }
        }
        catch(Exception e)
        {
            gotLatLng = false;
            e.printStackTrace();
        }
        return gotLatLng;
    }

}
