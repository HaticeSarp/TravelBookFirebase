package com.example.travelbook.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.travelbook.Fragment.HomeFragment;
import com.example.travelbook.Model.LocationModel;
import com.example.travelbook.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private LocationModel lmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Location");//Tablo ismi

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
        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (info.matches("new")) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    SharedPreferences sharedPreferences = MapsActivity.this.getSharedPreferences("com.example.travelbook", MODE_PRIVATE);
                    boolean firstTimeCheck = sharedPreferences.getBoolean("notFirstTime", false);

                    if (!firstTimeCheck) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        sharedPreferences.edit().putBoolean("notFirstTime", true).apply();
                    }


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (Build.VERSION.SDK_INT >= 23)
            {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else
                    {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    mMap.clear();

                    Location lastlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastlocation != null) {
                        LatLng lastUserLocation = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15));
                    }
                }
            }
            else
                {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastlocation != null) {
                    LatLng lastUserLocation = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15));
                }
            }

        }else{
           mMap.clear();
        /*    int position=intent.getIntExtra("position",0);
            LatLng location=new LatLng(.latitude,MainActivity.locations.get(position).longitude);
            String placeName=MainActivity.names.get(position);

            mMap.addMarker(new MarkerOptions().title(placeName).position(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(grantResults.length>0){
            if(requestCode==1){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);


                    Intent intent=getIntent();
                    String info=intent.getStringExtra("info");

                    if(info.matches("new")){
                        Location lastlocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(lastlocation!=null){
                            LatLng lastUserLocation=new LatLng(lastlocation.getLatitude(),lastlocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));
                        }
                    }
                    else{
                   /*     mMap.clear();
                        int position=intent.getIntExtra("position",0);
                        LatLng location=new LatLng(MainActivity.locations.get(position).latitude,MainActivity.locations.get(position).longitude);
                        String placeName=MainActivity.names.get(position);

                        mMap.addMarker(new MarkerOptions().title(placeName).position(location));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));*/

                    }
                }
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "";

        try {

            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addressList != null && addressList.size() > 0) {
                if (addressList.get(0).getThoroughfare() != null) {
                    address += addressList.get(0).getThoroughfare()+" ";

                    if (addressList.get(0).getSubThoroughfare() != null) {
                        address += addressList.get(0).getThoroughfare();
                    }
                }
            }
            else {
                address = "New  place";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        try {
            insertLocation(latLng.latitude,latLng.longitude);
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(getLayoutInflater().getContext(),"Problem",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    void insertLocation(Double latitude,Double longitude)
    {
        String id=databaseReference.push().getKey();
        lmodel=new LocationModel(id,latitude,longitude);
        databaseReference.child(id).setValue(lmodel);
        Toast.makeText(getLayoutInflater().getContext(),"Konum kayıt edildi",Toast.LENGTH_LONG).show();
    }
}
