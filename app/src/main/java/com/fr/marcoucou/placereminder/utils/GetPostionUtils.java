package com.fr.marcoucou.placereminder.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by sirisak on 11/06/16.
 */
public class GetPostionUtils implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long POLLING_FREQ = 1000 * 5;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 2;

    private Location mLastLocation = null;
    private GoogleApiClient mGoogleApiClient;
    private ResultLastLocation resultLastLocation;
    private Context context;

    public GetPostionUtils(Context context, ResultLastLocation resultLastLocation){
        // Create an instance of GoogleAPIClient.
        this.resultLastLocation = resultLastLocation;
        this.context = context;
    }


    public void initializingPosition(GoogleApiClient mGoogleApiClient){
        this.mGoogleApiClient = mGoogleApiClient;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    public void gettingLastPosition(){
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }catch (SecurityException e){

        }

        if (mLastLocation != null){
            resultLastLocation.connectedLocation(mLastLocation);
        }else{
            resultLastLocation.disconnectedLocation();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
            try {
                LocationRequest request = createLocationRequest();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        mLastLocation = location;
                    }
                });
            } catch (SecurityException e) {

            }
    }

    public LocationRequest createLocationRequest(){
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //request.setNumUpdates(1);
        request.setInterval(POLLING_FREQ);
        request.setFastestInterval(FASTEST_UPDATE_FREQ);
        return request;
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        resultLastLocation.disconnectedLocation();
    }
}
