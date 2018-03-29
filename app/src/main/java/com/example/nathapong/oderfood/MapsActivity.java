package com.example.nathapong.oderfood;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nathapong.oderfood.Common.Common;
import com.example.nathapong.oderfood.Model.Order;
import com.example.nathapong.oderfood.Model.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private FirebaseDatabase database;
    private DatabaseReference requestRef;

    private Request request;
    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database = FirebaseDatabase.getInstance();
        requestRef = database.getReference("Request");

        if (getIntent() != null) {
            requestId = getIntent().getStringExtra("requestId");

            setMarkerLocation(requestId);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void setMarkerLocation(String strRequestId){

        requestRef.child(strRequestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                request = dataSnapshot.getValue(Request.class);

                String name = request.getName();
                String address = request.getAddressDetail();
                double latitude = request.getLatitude();
                double longitude = request.getLongitude();

                LatLng currentLocation = new LatLng(latitude, longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentLocation);
                markerOptions.title("คุณ " + name);
                markerOptions.snippet(address);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                Marker marker = mMap.addMarker(markerOptions);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
            }
            @Override

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
