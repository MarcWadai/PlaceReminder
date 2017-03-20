package com.fr.marcoucou.placereminder.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fr.marcoucou.placereminder.DBLite.PlacesDataSource;
import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.model.Places;
import com.fr.marcoucou.placereminder.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LatLng latLng = new LatLng(0.0,0.0);
    private ProgressDialog progressBar;
    private PlacesDataSource placesDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        placesDataSource = new PlacesDataSource(this);
        placesDataSource.open();
        progressBar = new ProgressDialog(MapActivity.this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getString(R.string.progress_message_position));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(this.googleMap !=null){
            setMarkerLocation(this.googleMap);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        checkPermissions();
        this.googleMap = googleMap;
        setMarkerLocation(googleMap);
        progressBar.dismiss();
    }

    private void setMarkerLocation(GoogleMap googleMap){
        try {
            googleMap.setMyLocationEnabled(true);
            if (getIntent().hasExtra("listaddress")){
                int category = getIntent().getIntExtra("listaddress", 1);
                ArrayList<Places> placeslist = placesDataSource.getPlacesFromCategory(category);
                for (int i=0; i< placeslist.size(); i++){
                    addressSearchResult(placeslist.get(i),Constants.MAP_ZOOM_LARGE);
                }
            }
            else{
                String adress = getIntent().getStringExtra("address");
                ArrayList<Places> places = placesDataSource.getPlacesFromAddress(adress);
                addressSearchResult(places.get(0), Constants.MAP_ZOOM);
            }
        } catch(SecurityException e){
            Toast.makeText(getApplicationContext(),getString(R.string.toast_permission_geoloc),Toast.LENGTH_LONG);
            Log.e("Security", "Security exception " + e);
        }
    }

    public void addressSearchResult(Places places, float mapZoom){
        Boolean adressResult = getLatLongFromAddress(places.getAdresse());
        if (adressResult){
            Bitmap resizedbitmap = Bitmap.createScaledBitmap(places.getPlaceImage(), places.getPlaceImage().getWidth()/4, places.getPlaceImage().getHeight()/4 , false);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resizedbitmap);
            googleMap.addMarker(new MarkerOptions().position(latLng)
                    .title(places.getTitle())
                    .snippet(places.getAdresse())
                    .icon(bitmapDescriptor)
            );
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), mapZoom));
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_noplace_found), Toast.LENGTH_LONG);
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

    public boolean checkPermissions(){
        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(Constants.requiredLocationPermissions)) {
            Toast.makeText(this, "Please grant all permissions", Toast.LENGTH_LONG).show();
            goToSettings();
            return false;
        }
        else{
            return true;
        }
    }

    public boolean hasPermissions( @NonNull String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getApplicationContext(),permission))
                return false;
        return true;
    }

    public void goToSettings() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        getApplicationContext().startActivity(i);
    }
}
