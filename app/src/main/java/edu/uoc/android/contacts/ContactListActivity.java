package edu.uoc.android.contacts;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import edu.uoc.android.contacts.manager.FirebaseContactManager;
import edu.uoc.android.contacts.model.Contact;
import edu.uoc.android.contacts.model.GeoLocation;

public class ContactListActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Get all contacts from FirebaseContactManager class
        contactList = FirebaseContactManager.getInstance().getAllContacts();
        Log.d("Lista", contactList.toString());

        for (int i= 0; i<contactList.size(); i++) {

            Log.d("Dentro de for: ", String.valueOf(i));

            Contact contact = contactList.get(i);

            GeoLocation geoLocation = contact.getLocation();
            String name = contact.getName();

            // Add a marker in Contact Location and move the camera
            LatLng latLng = new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
}
