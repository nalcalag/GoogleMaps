package edu.uoc.android.contacts;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import edu.uoc.android.contacts.manager.FirebaseContactManager;
import edu.uoc.android.contacts.model.Contact;
import edu.uoc.android.contacts.model.GeoLocation;

public class ContactListActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    List<Contact> contactList = new ArrayList<>();
    SharedPreferences sharedPref;
    String sharedPreferenceZoom = "edu.uoc.android.contacts.zoom";
    float zoom = 12.0f;
    float z;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation = null;
    BreakIterator mLatitudeText = null;
    BreakIterator mLongitudeText = null;
    //Request code
    private final int REQUEST_PERMISSION_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Get Shared Preferences
        sharedPref = this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        // Set Zoom Preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(sharedPreferenceZoom, zoom);
        editor.commit();
        // Ask for location permission
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get all contacts from FirebaseContactManager class
        contactList = FirebaseContactManager.getInstance().getAllContacts();
        Log.d("Lista", contactList.toString());

        // Set Terrain map
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // Get SharedPreference zoom
        int defaultValue = 0;
        z = sharedPref.getFloat(sharedPreferenceZoom, defaultValue);
        // Add Zoom Controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Set location button
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            //Not in api-23, no need to prompt
            mMap.setMyLocationEnabled(true);
        }

        for (int i = 0; i < contactList.size(); i++) {

            Contact contact = contactList.get(i);

            GeoLocation geoLocation = contact.getLocation();
            String name = contact.getName();

            // Get contact location
            LatLng latLng = new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude());
            //Create Marker
            MarkerOptions marker = new MarkerOptions().position(latLng).title(name);
            // Set marker icon
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
            // Add contact marker
            mMap.addMarker(marker);

        }

        if (mLastLocation != null) {
            //Move the camera to the user's location and zoom in!
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, z));
        } else {
            //Move the camera to the first contact and zoom in!
            LatLng latLng = new LatLng(contactList.get(0).getLocation().getLatitude(), contactList.get(0).getLocation().getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, z));
        }


    }

    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            }
            return;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                        if (mLastLocation != null) {
                            //Move the camera to the user's location and zoom in!
                            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, z));
                        } else {
                            //Move the camera to the first contact and zoom in!
                            LatLng latLng = new LatLng(contactList.get(0).getLocation().getLatitude(), contactList.get(0).getLocation().getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, z));
                        }
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
