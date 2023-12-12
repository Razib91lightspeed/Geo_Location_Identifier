package com.example.mobile_finder;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationHandler {
    private FusedLocationProviderClient fusedLocationClient;
    private Context context;

    public LocationHandler(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getLastLocation(final LocationCallback callback) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    callback.onLocationReceived(location);
                }
            });
        }
    }

    public interface LocationCallback {
        void onLocationReceived(Location location);
    }
}


