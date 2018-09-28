package com.example.jatinderkumar.googleapi01;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView txtLatitude, txtLongitude, txtPhysicalAddress;
    Button btnLocation, btnMap;
    Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLocation = findViewById(R.id.btnShowLocation);
        btnMap = findViewById(R.id.btnShowMap);

        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        txtPhysicalAddress = findViewById(R.id.txtPyaddress);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWhereIAm();
            }
        });

    }

    private void getWhereIAm() {
        Toast.makeText(this,"I am At This Location",Toast.LENGTH_LONG).show();
    }

    private void getMyLocation() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },1);
        }else{
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        lastLocation = location;
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        txtLatitude.setText(latitude+"");
                        txtLongitude.setText(longitude+"");
                        Geocoder geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
                        try {
                            List<Address> locationList = geocoder.getFromLocation(latitude,longitude,1);
                            if(locationList.size()>0){
                                Address address = locationList.get(0);
                                String country = address.getCountryCode();
                                String pyAdd = address.getCountryName();
                                txtPhysicalAddress.setText(country+"-"+pyAdd+"\n"+address.getAdminArea()+","+
                                        address.getSubAdminArea()+","+address.getAddressLine(0));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getMyLocation();
                }else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
