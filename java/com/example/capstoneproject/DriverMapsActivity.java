package com.example.capstoneproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String toLocation;
    String fromDriver;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    LocationManager locationManager;
    Button btnBack;
    String driverId;
    Button btnDriver;
    MarkerOptions marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnDriver = (Button) findViewById(R.id.btnDriver);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMapsActivity.this, MainActivity.class);
                DriverMapsActivity.this.startActivity(intent);
            }
        });

        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();

                bundle1.putString("driverId", driverId);

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                ViewDriverFragment children2Fragment = new ViewDriverFragment();
                children2Fragment.setArguments(bundle1);
                fragmentTransaction1.replace(R.id.fragment_container, children2Fragment);
                fragmentTransaction1.commit();

                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.llButtons).setVisibility(View.GONE);
            }

        });

        Bundle extras = getIntent().getExtras();
        driverId = extras.getString("driverId");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("DriverLocation").child(driverId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("latitude")) {
                    toLocation = snapshot.child("latitude").getValue().toString() + "," + snapshot.child("longitude").getValue().toString();
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = mFirebaseDatabase.getReference().child("SchoolLocation");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild("latitude")) {

                                double longitude2 = Double.parseDouble(snapshot.child("longitude").getValue().toString());
                                double latitude2 = Double.parseDouble(snapshot.child("latitude").getValue().toString());
                                double longitude1 = Double.parseDouble(toLocation.split(",")[1]);
                                double latitude1 = Double.parseDouble(toLocation.split(",")[0]);
                                float zoom = mMap.getCameraPosition().zoom;
                                zoom = 22;
                                LatLng latLang = new LatLng(latitude1, longitude1);
                                Geocoder geocoder = new Geocoder(getApplicationContext());
                                try {
                                    List<Address> addressList = geocoder.getFromLocation(latitude1, longitude1, 1);
                                    mMap.clear();
                                    mMap.addMarker(new MarkerOptions().position(latLang).title("Driver Location")).showInfoWindow();
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom));
                                    latLang = new LatLng(latitude2, longitude2);
                                    mMap.addMarker(new MarkerOptions().position(latLang).title("School Location")).showInfoWindow();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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
            LatLng cse = new LatLng(31.255209, 75.703470);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cse,15));
            mMap.addMarker(new MarkerOptions().position(cse).title("Driver Location"));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            mMap.setMyLocationEnabled(true);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        // TODO Auto-generated method stub
                        mMap.clear();
                        marker = new MarkerOptions().position(point);
                        mMap.addMarker(marker);
                    }
                });
            }
}

