package com.fr.marcoucou.placereminder.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
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
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        progressBar = new ProgressDialog(MapActivity.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Getting the address position ");
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
            String adress = getIntent().getStringExtra("address");
            Boolean adressResult = getLatLongFromAddress(adress);
            if (adressResult){
                googleMap.addMarker(new MarkerOptions().position(latLng).title(adress));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), Constants.MAP_ZOOM));
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

    public boolean checkPermissions(){
        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(Constants.requiredPermissions)) {
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
