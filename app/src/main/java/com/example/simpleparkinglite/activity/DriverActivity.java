package com.example.simpleparkinglite.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.simpleparkinglite.config.ConfigFirebase;
import com.example.simpleparkinglite.model.Destiny;
import com.example.simpleparkinglite.model.Parking;
import com.example.simpleparkinglite.model.Request;
import com.example.simpleparkinglite.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simpleparkinglite.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DriverActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseAuth auth;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private EditText fieldFestiny;
    private LatLng myLocale;
    private FloatingActionButton floatingLocale;
    private FloatingActionButton floatingList;
    private FloatingActionButton floatingMap;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializing();

        auth = ConfigFirebase.getFirebaseAuth();
        reference = ConfigFirebase.getFirebaseDatabase();

        floatingLocale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParking();
                retrieveLocation();
            }
        });

        floatingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListActivity.class));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        retrieveLocation();
    }

    private void retrieveLocation() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                myLocale = new LatLng(latitude, longitude);

                mMap.clear();
                mMap.addMarker(
                        new MarkerOptions()
                                .position(myLocale)
                                .title("Meu local")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario))
                );

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocale, 20));

                getParking();
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    10,
                    locationListener
            );
        }
    }

    private void newLocalion(final String lat, final String longi) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onLocationChanged(Location location) {
                double latitude = Double.parseDouble(lat);;
                double longitude = Double.parseDouble(longi);
                myLocale = new LatLng(latitude, longitude);

                mMap.clear();
                mMap.addMarker(
                        new MarkerOptions()
                                .position(myLocale)
                                .title("Novo local")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.usuario))
                );

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocale, 20));

                getParking();

                floatingMap.setVisibility(View.VISIBLE);
                floatingMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String latLong = lat + ',' + longi;
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latLong+"&mode=d");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    10,
                    locationListener
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair:
                auth.signOut();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void destiny(View view) {

        String textDestiny = fieldFestiny.getText().toString();

        if (!textDestiny.equals("") && textDestiny != null){
            Address addressDestiny = retrieveAddress(textDestiny);

            if (addressDestiny != null) {
                final Destiny destinyClass = new Destiny();

                destinyClass.setCity(addressDestiny.getSubAdminArea());
                destinyClass.setZipCode(addressDestiny.getPostalCode());
                destinyClass.setNeighborhood(addressDestiny.getSubLocality());
                destinyClass.setStreet(addressDestiny.getThoroughfare());
                destinyClass.setNumber(addressDestiny.getFeatureName());
                destinyClass.setLatitude(String.valueOf(addressDestiny.getLatitude()));
                destinyClass.setLongitude(String.valueOf(addressDestiny.getLongitude()));

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cidade: " + (destinyClass.getCity() == null ? "" : destinyClass.getCity()));
                stringBuilder.append("\nRua: " + (destinyClass.getStreet() == null ? "" : destinyClass.getStreet()));
                stringBuilder.append("\nBairro: " + (destinyClass.getNeighborhood() == null ? "" : destinyClass.getNeighborhood()));
                stringBuilder.append("\nNúmero: " + (destinyClass.getNumber() == null ? "" : destinyClass.getNumber()));
                stringBuilder.append("\nCep: " + (destinyClass.getZipCode() ==  null ? "" : destinyClass.getZipCode()));

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Confirme o endereço!")
                        .setMessage(stringBuilder)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                save(destinyClass);
                                newLocalion(destinyClass.getLatitude(), destinyClass.getLongitude());
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Endereço não encontrado!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }else {
            Toast.makeText(
                    DriverActivity.this,
                    "Preencha o local desejado, para realizar a pesquisa",
                    Toast.LENGTH_LONG
            ).show();
        }

    }

    private void save(Destiny destinySave){
        Destiny request = new Destiny();

        request.setStreet(destinySave.getStreet());
        request.setNeighborhood(destinySave.getNeighborhood());
        request.setCity(destinySave.getCity());
        request.setNumber(destinySave.getNumber());
        request.setZipCode(destinySave.getZipCode());
        request.setLongitude(destinySave.getLongitude());
        request.setLatitude(destinySave.getLatitude());

        User userLogged = ConfigFirebase.getUser();
        userLogged.setLatitude(String.valueOf(myLocale.latitude));
        userLogged.setLongitude(String.valueOf(myLocale.longitude));
        request.saveRequest();
    }

    private Address retrieveAddress(String address){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listAddress = geocoder.getFromLocationName(address, 1);
            if (listAddress != null && listAddress.size() >= 1) {
                Address addressGet = listAddress.get(0);
                double latitude = addressGet.getLatitude();
                double longitude = addressGet.getLongitude();

                return addressGet;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getParking(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        DatabaseReference parkingRef = reference.child("parkings");
        parkingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Parking parking = ds.getValue(Parking.class);

                    double latitude = Double.parseDouble(parking.getLatitude());;
                    double longitude = Double.parseDouble(parking.getLongitude());
                    myLocale = new LatLng(latitude, longitude);

                    mMap.addMarker(
                            new MarkerOptions()
                                    .position(myLocale)
                                    .title("Estacionamento: " + parking.getNome() + " | Vagas: " + parking.getVagas())
                    );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializing(){

        setContentView(R.layout.activity_driver);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        fieldFestiny = findViewById(R.id.destiny);
        auth = ConfigFirebase.getFirebaseAuth();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        floatingLocale = findViewById(R.id.floatingLocale);
        floatingList = findViewById(R.id.floatingActionButton2);
        floatingMap = findViewById(R.id.floatingMap);

    }
}
