package com.example.mobile_finder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL; // as required
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // Constants for permission requests
    private static final int LOCATION_PERMISSION_REQUEST = 101;
    private static final int CAMERA_PERMISSION_REQUEST = 100;

    // UI elements
    private Button scanButton;
    private TextView scannedLocationLabel;
    private TextView scannedLatitude;
    private TextView scannedLongitude;
    private CardView scannedLocationAddressCard;
    private TextView scannedLocationAddressInfo;
    private CardView myAddressCard;
    private TextView myAddressInfo;
    private CardView distanceCard;
    private TextView distanceInfo;
    private TextView myLocationLabel;
    private TextView myLocationLatitude;
    private TextView myLocationLongitude;
    private TextView scannedLocationInfo;

    // Location-related components
    private LocationHandler locationHandler;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        scanButton = findViewById(R.id.scanButton);
        scannedLocationLabel = findViewById(R.id.scannedLocationLabel);
        scannedLatitude = findViewById(R.id.scannedLatitude);
        scannedLongitude = findViewById(R.id.scannedLongitude);
        scannedLocationAddressCard = findViewById(R.id.scannedLocationAddressCard);
        scannedLocationAddressInfo = scannedLocationAddressCard.findViewById(R.id.scannedLocationAddressInfo);
        myAddressCard = findViewById(R.id.myAddressCard);
        myAddressInfo = myAddressCard.findViewById(R.id.myAddressInfo);
        distanceCard = findViewById(R.id.distanceCard);
        distanceInfo = distanceCard.findViewById(R.id.distanceInfo);
        myLocationLabel = findViewById(R.id.myLocationLabel);
        myLocationLatitude = findViewById(R.id.myLocationLatitude);
        myLocationLongitude = findViewById(R.id.myLocationLongitude);
        scannedLocationInfo = findViewById(R.id.scannedLocationInfo);

        // Initialize location-related components
        geocoder = new Geocoder(this, Locale.ITALY);
        locationHandler = new LocationHandler(this);

        // Set click listener for the scan button
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestCameraPermission();
            }
        });

        // Check and request location permission
        checkAndRequestLocationPermission();
    }

    // Check and request location permission
    private void checkAndRequestLocationPermission() {
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            // Location permission already granted, proceed with your location-related code.
        } else {
            // Request location permission.
            ActivityCompat.requestPermissions(this, new String[]
                    {
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }
    }

    // Handle the result of the QR code scanning activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {
            IntentResult result = IntentIntegrator.parseActivityResult
                    (requestCode, resultCode, data);
            if (result != null) {
                String geoUri = result.getContents();
                if (geoUri != null && !geoUri.isEmpty()) {
                    processGeoUri(geoUri);
                }
            }
        }
    }

    // Check and request camera permission
    private void checkAndRequestCameraPermission() {
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            // Camera permission already granted, proceed with scanning.
            scanQRCode();
        } else {
            // Request camera permission.
            ActivityCompat.requestPermissions(this, new String[]
                    {
                    Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }
    }

    // Initiate QR code scanning
    private void scanQRCode() {
        new IntentIntegrator(this).initiateScan();
    }

    // Handle the result of permission requests
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Camera permission granted, proceed with scanning.
                scanQRCode();
            } else {
                // Camera not granted for scanning
            }
        }
    }

    // Process the scanned Geo URI
    public void processGeoUri(String geoUri) {
        if (geoUri.startsWith("geo:")) {
            try {
                // Custom parsing of geoUri
                String[] uriParts = geoUri.substring(4).split(",");
                if (uriParts.length == 2) {
                    double latitude = Double.parseDouble(uriParts[0]);
                    double longitude = Double.parseDouble(uriParts[1]);

                    // Refactor the code into methods
                    handleScannedLocation(latitude, longitude);

                    // Extract latitude and longitude using URL
                    URL url = new URL(geoUri);
                    double extractedLatitude = extractLatitude(url);
                    double extractedLongitude = extractLongitude(url);

                    // Update the UI with the extracted latitude and longitude
                    updateUIWithExtractedValues(extractedLatitude, extractedLongitude);
                }
            }
            catch (NumberFormatException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUIWithExtractedValues(double latitude, double longitude) {
        //  layout with the following IDs
        TextView extractedLatitudeTextView = findViewById(R.id.extractedLatitudeTextView);
        TextView extractedLongitudeTextView = findViewById(R.id.extractedLongitudeTextView);

        //  TextViews
        extractedLatitudeTextView.setText(" : " + latitude);
        extractedLongitudeTextView.setText(": " + longitude);
    }



    // Extract latitude from URL
    private double extractLatitude(URL url) {
        String path = url.getPath().substring(1); // Remove the leading '/'
        String[] pathParts = path.split(",");
        return Double.parseDouble(pathParts[0]);
    }

    // Extract longitude from URL
    private double extractLongitude(URL url) {
        String path = url.getPath().substring(1); // Remove the leading '/'
        String[] pathParts = path.split(",");
        return Double.parseDouble(pathParts[1]);
    }

    // Handle the scanned location
    private void handleScannedLocation(double latitude, double longitude) {
        // Display latitude and longitude in the placeholders
        scannedLatitude.setText(String.valueOf(latitude));
        scannedLongitude.setText(String.valueOf(longitude));

        // Get address information for the scanned location
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            handleAddressInformation(addresses);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error cases (e.g., log the error, show a message to the user)
        }
    }

    // Handle address information
    private void handleAddressInformation(List<Address> addresses) {
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            String addressText = getAddressText(address);
            if (addressText != null) {
                scannedLocationAddressInfo.setText(addressText);

                // Calculate the distance
                calculateDistance(address);
            }
        }
    }

    // Get formatted address text
    private String getAddressText(Address address) {
        return (address != null && address.getAddressLine(0) != null) ? address.getAddressLine(0) : null;
    }

    // Calculate and display the distance between the scanned location and the current device location
    private void calculateDistance(Address scannedAddress) {
        // Create a new Location object for the scanned location
        Location scannedLocation = new Location("ScannedLocation");
        scannedLocation.setLatitude(scannedAddress.getLatitude());
        scannedLocation.setLongitude(scannedAddress.getLongitude());

        // Get the last known location of the device using the LocationHandler
        locationHandler.getLastLocation(location -> {
            // Extract latitude and longitude of the current device location
            double myLatitude = location.getLatitude();
            double myLongitude = location.getLongitude();

            // Display current device location coordinates in UI
            myLocationLatitude.setText(String.valueOf(myLatitude));
            myLocationLongitude.setText(String.valueOf(myLongitude));

            // Create a new Location object for the current device location
            Location myLocation = new Location("MyLocation");
            myLocation.setLatitude(myLatitude);
            myLocation.setLongitude(myLongitude);

            // Calculate the distance in meters between the scanned location and the current device location
            double distanceInMeters = myLocation.distanceTo(scannedLocation);

            // Convert the distance to kilometers
            double distanceInKilometers = distanceInMeters / 1000.0;

            // Round the distance to two decimal places
            double roundedDistance = Math.round(distanceInKilometers * 100.0) / 100.0;

            // Format the distance text
            String distanceText = " " + roundedDistance + " km";

            // Display the calculated distance in UI
            distanceInfo.setText(distanceText);

            try {
                // Get the address information for the current device location
                List<Address> myAddresses = geocoder.getFromLocation(myLatitude, myLongitude, 1);

                // Handle and display the address information for the current device location
                handleMyAddressInformation(myAddresses);
            } catch (IOException e) {
                // Handle IOException, for example, log the error
                e.printStackTrace();
            }

            // Update UI with the label indicating the displayed information is about the current device location
            String myLocationText = "My Location ";
            myLocationLabel.setText(myLocationText);
        });
    }

    // Handle and display information about the user's own address.
    private void handleMyAddressInformation(List<Address> myAddresses) {
        // Check if the list of addresses is not null and not empty
        if (myAddresses != null && !myAddresses.isEmpty()) {
            // Get the first address from the list (assuming the list is ordered by relevance)
            Address myAddress = myAddresses.get(0);

            // Get a formatted text representation of the user's address
            String myAddressText = getAddressText(myAddress);

            // Check if the formatted address text is not null
            if (myAddressText != null) {
                // Set the formatted address text to the corresponding TextView in the UI
                myAddressInfo.setText(myAddressText);
            }
        }
    }

}
