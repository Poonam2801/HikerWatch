package com.example.poonamgupta2801.hikerwatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult ( requestCode, permissions, grantResults );

        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED) {

           startListening ();
        }
    }

    public void startListening() {

        if(ContextCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION )
                ==PackageManager.PERMISSION_GRANTED){

            Location location=locationManager.getLastKnownLocation ( LocationManager.GPS_PROVIDER );
            Log.i("Permission Status", "Permission granted");

        }

    }
    public void updateLocationInfo(Location location) {

        TextView latitudeTextView = (TextView) findViewById ( R.id.latTextView );
        TextView longitudeTextView = (TextView) findViewById ( R.id.latTextView );
        TextView accuracyTextView = (TextView) findViewById ( R.id.accuracyTextView );
        TextView altitudeTextView = (TextView) findViewById ( R.id.altitudeTextView );
        TextView addressTextView = (TextView) findViewById ( R.id.addressTextView );

        latitudeTextView.setText ( "Latitude: " + location.getLatitude () );
        longitudeTextView.setText ( "Longitude: " + location.getLongitude () );
        accuracyTextView.setText ( "Accuracy: " + location.getAccuracy () );
        altitudeTextView.setText ( "Altitude: " + location.getAltitude () );

        Geocoder geocoder = new Geocoder ( getApplicationContext (), Locale.getDefault () );

        try {

            String address= "Couldn't find address";

            List<Address> addressList = geocoder.getFromLocation ( location.getLatitude (), location.getLongitude (), 1);

            if(addressList!=null&&addressList.size ()>0){

                address="Address: \n" ;

                if(addressList.get(0).getSubThoroughfare ()!=null){
                    address+=addressList.get(0).getSubThoroughfare ()+" ";
                }
                if(addressList.get(0).getThoroughfare ()!=null){
                    address+=addressList.get(0).getThoroughfare ()+"\n ";
                }

                if(addressList.get(0).getLocality ()!=null){
                    address+=addressList.get(0).getLocality ()+"\n ";
                }

                if(addressList.get(0).getPostalCode ()!=null){
                    address+=addressList.get(0).getPostalCode ()+"\n ";
                }

                if(addressList.get(0).getCountryName ()!=null){
                    address+=addressList.get(0).getCountryName ()+"\n ";
                }



                Log.i ( "PlaceINFO", addressList.get ( 0 ).toString () );

                Toast.makeText ( getApplicationContext (),addressList.get ( 0 ).toString (),Toast.LENGTH_SHORT ).show ();
            } else   {

                Toast.makeText ( getApplicationContext (),"Could not find",Toast.LENGTH_SHORT ).show ();
                Log.i ( "PlaceINFO", "Couldn't get" );

            }

            addressTextView.setText ( address );


        } catch (IOException e) {
            e.printStackTrace ();
        }

        Log.i ( "LocationINFO", location.toString () );
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        TextView appTextName= (TextView)findViewById ( R.id.apptextName );

        locationManager= (LocationManager)this.getSystemService ( Context.LOCATION_SERVICE );

        locationListener=new LocationListener () {
            @Override
            public void onLocationChanged(Location location) {
               updateLocationInfo ( location );

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

        if(Build.VERSION.SDK_INT<3){
            startListening ();
        } else {

            if(ContextCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ){

             //Ask for permission
                ActivityCompat.requestPermissions ( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1 );

            } else{
                locationManager.requestLocationUpdates ( LocationManager.GPS_PROVIDER,0,0,locationListener );

                Location location=locationManager.getLastKnownLocation ( LocationManager.GPS_PROVIDER );

                if(location!=null){

                    updateLocationInfo ( location );
                }

            }

        }


    }
}
