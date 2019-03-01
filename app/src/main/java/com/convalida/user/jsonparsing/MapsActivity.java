package com.convalida.user.jsonparsing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerDragListener,GoogleMap.OnMapLongClickListener{

    private GoogleMap mMap;
    private double longitude, latitude;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
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

        // Add a marker in Sydney and move the camera
        LatLng coordinates = new LatLng(-34, 151);
      //  mMap.addMarker(new MarkerOptions().position(coordinates).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    public void onStart(){
        googleApiClient.connect();
        super.onStart();
    }

    public void onStop(){
        googleApiClient.disconnect();
        super.onStop();
    }

    private void moveMap(){
        //double latitude=32.553616;
        //double longitude=-83.66848099999999;
        double latitude=getIntent().getDoubleExtra("lat",0);
        double longitude=getIntent().getDoubleExtra("lon",0);
        LatLng coordinates=new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(coordinates).draggable(true));
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(coordinates,20);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        mMap.animateCamera(cameraUpdate);
       //  mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
     //  mMap.animateCamera(CameraUpdateFactory.zoomTo(25));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        moveMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude=marker.getPosition().latitude;
        longitude=marker.getPosition().longitude;
        moveMap();
    }
}
