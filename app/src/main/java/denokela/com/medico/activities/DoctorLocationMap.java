package denokela.com.medico.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.GeoApiContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import denokela.com.medico.R;

public class DoctorLocationMap extends AppCompatActivity {

    EditText editText_Address;
    ImageView imageView_mylocation;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final float DEFAULT_ZOOM = 15f;
    private static int ERROR_DIALOG_REQUEST = 998;
    private Boolean LocationPermissionGranted = false;

    GoogleMap mMap;
    double latitude,longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    GeoApiContext mGeoApiContext = null;

    private static final String TAG = "DoctorLocationMap";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_location_map);

        editText_Address = findViewById(R.id.editText_address);
        imageView_mylocation = findViewById(R.id.dlm_image_view_mylocation);

        if (isServicesOk()) {
            getLocationPermission();
        }

        String address = getIntent().getStringExtra("Address");
        editText_Address.setText(address);

    }


    //1. Check if the user is using a correct version of google play services
    public Boolean isServicesOk() {
        Log.d(TAG, "isServicesOk: Checking Google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            //Everything is fine
            Log.d(TAG, "isServicesOk: Google play services is okay");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //Error occured
            Log.d(TAG, "isServicesOk: Error occured");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Map requests can't be made", Toast.LENGTH_SHORT).show();
        }
        return false;

    }

    //2. Getting Location permission
    public void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        Log.d(TAG, "getLocationPermission: At getlocation permission ");
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission: glp Check1");
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocationPermission: glp Check2");
                LocationPermissionGranted = true;
                initMap();

            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }

        } else {
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            //For activity view use ActivityCompat.requestPermission(this,....)
            //requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LocationPermissionGranted = false;
        Log.d(TAG, "onRequestPermissionsResult: Reached request permission result");
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            LocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permmission Failed");
                            return;
                        }
                    }
                    LocationPermissionGranted = true;
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                    initMap();
                }
            }
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.doctorAddressmap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Toast.makeText(getApplicationContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onMapReady: Map is ready at this method");
                mMap = googleMap;

                //Centering the map to current location and adding the marker
                if (LocationPermissionGranted) {
                    getDeviceLocation();

                    mMap.setMyLocationEnabled(true);
                    //Disable default location button
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    init();
                }
            }
        });
    }


    //3. Get Device  Location
    public void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: Getting current device location");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(LocationPermissionGranted){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Found Location");
                            Location currentLocation = (Location) task.getResult();
                            latitude = currentLocation.getLatitude();
                            longitude = currentLocation.getLongitude();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM, "My Location");
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(DoctorLocationMap.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException exc){
            Log.e(TAG, "getDeviceLocation: Security Exception:"+ exc.getMessage());
        }

    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: Moving the Camera to: lat: "+ latLng.latitude + " , lng: "+ latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

    }

    private void init(){
        Log.d(TAG, "init: initializing");
        if( mGeoApiContext== null){
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.Google_Directions_Key))
                    .build();

        }

        geolocate();
        editText_Address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionid, KeyEvent keyEvent) {
                if(actionid == EditorInfo.IME_ACTION_SEARCH || actionid == EditorInfo.IME_ACTION_DONE ||
                keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //Execute Method for searching
                    geolocate();
                }
                return false;
            }
        });
        imageView_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });


    }


    private void geolocate() {
        Log.d(TAG, "geolocate: geolocating");
        String searchString = editText_Address.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName("Abuja",1);
        }catch(IOException e){
            Log.e(TAG, "geolocate: IOException: " + e.getMessage());
        }

        if(list.size()>0){
            Address address = list.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));

            Log.d(TAG, "geolocate: "+address.toString());
        }else{
            Toast.makeText(getApplicationContext(),"No location found", Toast.LENGTH_SHORT).show();
        }
    }
}
