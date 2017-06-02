package com.surajapps.gridclick;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by upret on 6/2/2017.
 */


public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap myMap;
    private boolean myLocPermission;
    private Location myLastLocation;
    private TextView latitudeText, longitudeText, addressText;
    private AddressResultReceiver myAddressResultReceiver;
    private GoogleApiClient googleApiClient;
    private static final int PERMISSIONS_ACCESS_FINE_LOCATION = 1024;
    private static final int SETTINGS_LOCATION_ON = 1111;
    private static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        latitudeText = (TextView) findViewById(R.id.latitude_textview);
        longitudeText = (TextView) findViewById(R.id.longitude_textview);
        addressText = (TextView) findViewById(R.id.address_textview);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();
        myAddressResultReceiver = new AddressResultReceiver(null);
    }

    // Builds the map when the Google Play services client is successfully connected.
    @Override
    public void onConnected(Bundle connectionHint) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // Location settings is on
            if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) { // Permission is granted by user
                myLocPermission = true;
                startMap();
            } else { // Permission not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_ACCESS_FINE_LOCATION);
            }
        } else { // Location settings is off
            new AlertDialog.Builder(this)
                    .setMessage("Please turn on location in settings and restart the app!")
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent locationSettings = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(locationSettings, SETTINGS_LOCATION_ON);
                        }
                    }).show();
        }
    }

    // Builds the map
    public void startMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Called when the map is ready to be used.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        updateMap();
        getDeviceLocation();
    }

    private void updateMap() {
        if (myMap == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myLocPermission = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);
        }
        if (myLocPermission) {
            myMap.setMyLocationEnabled(true);
            myMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            myMap.setMyLocationEnabled(false);
            myMap.getUiSettings().setMyLocationButtonEnabled(false);
            myLastLocation = null;
        }
    }

    // Gets the current location of the device, and positions the map's camera.
    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myLocPermission = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);
        }
        if (myLocPermission) {
            myLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(googleApiClient);

        }
        // Sets the map's camera position to current device's location
        if (myLastLocation != null) {
            LatLng latLng = new LatLng(myLastLocation.getLatitude(), myLastLocation.getLongitude());
            latitudeText.setText("Latitude: " + String.valueOf(myLastLocation.getLatitude()));
            longitudeText.setText("Longitude: " + String.valueOf(myLastLocation.getLongitude()));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            myMap.addMarker(new MarkerOptions()
                    .position(latLng));
            startAddressResolverIntent();
        }
    }

    public void startAddressResolverIntent() {
        Intent intent = new Intent(this, AddressResolverIntent.class);
        intent.putExtra(AddressResolverIntent.Constants.RECEIVER, myAddressResultReceiver);
        intent.putExtra(AddressResolverIntent.Constants.LOCATION_DATA_EXTRA, myLastLocation);
        startService(intent);
    }

    // Handles the result of the request for location permissions.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        myLocPermission = false;
        switch (requestCode) {
            case PERMISSIONS_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        myLocPermission = true;
                    }
                    startMap();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // Restarts the app after the user turn the location on in settings
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case (SETTINGS_LOCATION_ON):
                    startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    // Handles failure to connect to the Google Play services client.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.d(TAG, "Connection failed: " + result.getErrorCode());
    }


    // Handles suspension of the connection to the Google Play services client.
    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Connection suspended");
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private class AddressResultReceiver extends ResultReceiver {
        private AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            final String currentAddress = resultData.getString(AddressResolverIntent.Constants.RESULT_DATA_KEY);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addressText.setText("Address:" + currentAddress);
                }
            });
        }
    }
}

