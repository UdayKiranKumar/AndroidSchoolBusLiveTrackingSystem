package com.example.capstoneproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstoneproject.Models.LatitudeLongitude;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdminMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btnSave;
    Button btnBack;
    Button btnSearch;
    EditText etSearch;
    double home_long;
    double home_lat;
    LatLng latLng;
    MarkerOptions markerOptions;
    String addressText;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    String g;
    String latitude;
    String longitude;

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    String parentId;
    LocationManager locationManager;
    MarkerOptions marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        parentId = user.getUid();

        Firebase.setAndroidContext(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnBack = (Button) findViewById(R.id.btnBack);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateLocation();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(AdminMapsActivity.this, MainActivity.class);
                AdminMapsActivity.this.startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                g = etSearch.getText().toString().toLowerCase();

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference().child("MapLocation").child(g);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild("latitude") && snapshot.hasChild("longitude")) {
                            latitude = snapshot.child("latitude").getValue().toString();
                            longitude = snapshot.child("longitude").getValue().toString();


                            search2(Double.parseDouble(latitude),Double.parseDouble(longitude));
                        }
                        else{
                            Geocoder geocoder = new Geocoder(getBaseContext());
                            List<Address> addresses = null;

                            try {
                                // Getting a maximum of 3 Address that matches the input
                                // text
                                addresses = geocoder.getFromLocationName(g, 3);
                                if (addresses != null && !addresses.equals(""))
                                    search(addresses);

                            } catch (Exception e) {

                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    LatLng lpuclg = new LatLng(latitude, longitude);
                    marker = new MarkerOptions().position(lpuclg);
                    mMap.addMarker(new MarkerOptions().position(lpuclg));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lpuclg,15));

                    if (ActivityCompat.checkSelfPermission(AdminMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AdminMapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.removeUpdates(this);


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
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    LatLng lpuclg = new LatLng(latitude, longitude);
                    marker = new MarkerOptions().position(lpuclg);
                    mMap.addMarker(new MarkerOptions().position(lpuclg));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lpuclg,15));
                    if (ActivityCompat.checkSelfPermission(AdminMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AdminMapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.removeUpdates(this);

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
            });
        }

    }


    protected void search(List<Address> addresses) {

        Address address = (Address) addresses.get(0);
        home_long = address.getLongitude();
        home_lat = address.getLatitude();
        latLng = new LatLng(address.getLatitude(), address.getLongitude());

        addressText = String.format(
                "%s, %s",
                etSearch.getText().toString(), address.getCountryName());

        markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(addressText);

        mMap.clear();
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


    protected void search2(Double latitude, Double longitude) {

        home_long = longitude;
        home_lat = latitude;
        latLng = new LatLng(latitude, longitude);

        addressText = String.format(
                "%s",
                etSearch.getText().toString());

        markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(addressText);

        mMap.clear();
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void updateLocation(){



        if (marker != null) {

            Firebase ref = new Firebase(Config.FIREBASE_URL);
            LatitudeLongitude latitudeLongitude = new LatitudeLongitude();
            latitudeLongitude.setLatitude(String.valueOf(marker.getPosition().latitude));
            latitudeLongitude.setLongitude(String.valueOf(marker.getPosition().longitude));
            ref.child("SchoolLocation").setValue(latitudeLongitude);

            Toast toast=Toast.makeText(this, "Location Successfully Updated.", Toast.LENGTH_LONG);
            View view=toast.getView();
            view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
            TextView text =view.findViewById(android.R.id.message);
            text.setTextColor(Color.BLACK);
            toast.show();

        }
        else{
            Toast toast=Toast.makeText(this, "Please Pick Location.", Toast.LENGTH_LONG);
            View view=toast.getView();
            view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
            TextView text =view.findViewById(android.R.id.message);
            text.setTextColor(Color.BLACK);
            toast.show();
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

        // Add a marker in Sydney and move the camera
        LatLng lpuclg = new LatLng(31.252999, 75.704867);
        mMap.addMarker(new MarkerOptions().position(lpuclg).title("School Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lpuclg,15));

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
