package com.test.masterhardisk.deltastocktest.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by MasterHardisk on 11/6/18.
 */

public class GetLocation implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, android.location.LocationListener {

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 3030;
    private static int UPDATE_INTERVAL = 5000; // 5 sec
    private static int FATEST_INTERVAL = 2000; // 2 sec
    private static int DISPLACEMENT = 10; // 10 meters


    private LocationRequest mLocationRequest;
    private Context context;
    private Config config;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private LocationManager locationManager;
    public  Double latitude, longitude;


    public GetLocation(Context context) {
        this.context = context;

        config = new Config(context);

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        createLocationRequest();


    }

    private void getCoodenates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null) {
            Log.v("GETLOCATION", "Starting location...");
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            config.setUserLatitude(String.valueOf(latitude));
            config.setUserLongitude(String.valueOf(longitude));
            Log.v("GETLOCATION", latitude +" - "+longitude);

            getCity(latitude, longitude);
        }

        else Log.v("GETLOCATION", "ERROR");

    }

    private void getCity (Double latitude, Double longitude){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude,1);
            config.setUserCity(addresses.get(0).getLocality());
            config.setUserCP(addresses.get(0).getPostalCode());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCoodenates();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        getCoodenates();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.v("conexion", "conexion restablecida");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.v("conexion", "conexion perdida");
    }

}
