package denokela.com.medico;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

public class HealthCenterMaps extends Fragment {

    private static final String TAG = "HealthCenterMaps";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final float DEFAULT_ZOOM = 15f;
    private static int ERROR_DIALOG_REQUEST = 998;
    private Boolean LocationPermissionGranted = false;

    private FusedLocationProviderClient fusedLocationProviderClient;

    GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_health_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isServicesOk()) {
            getLocationPermission();
        }

    }


    //1. Check if the user is using a correct version of google play services
    public Boolean isServicesOk() {
        Log.d(TAG, "isServicesOk: Checking Google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if (available == ConnectionResult.SUCCESS) {
            //Everything is fine
            Log.d(TAG, "isServicesOk: Google play services is okay");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //Error occured
            Log.d(TAG, "isServicesOk: Error occured");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "Map requests can't be made", Toast.LENGTH_SHORT).show();
        }
        return false;

    }

    //2. Getting Location permission
    public void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        Log.d(TAG, "getLocationPermission: At getlocation permission ");
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocationPermission: glp Check1");
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocationPermission: glp Check2");
                LocationPermissionGranted = true;
                initMap();

            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }

        } else {

            //For activity view use ActivityCompat.requestPermission(this,....)
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
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
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Toast.makeText(getActivity(), "Map is Ready", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onMapReady: Map is ready at this method");
                mMap = googleMap;

                //Centering the map to current location and adding the marker
                if (LocationPermissionGranted) {
                    getDeviceLocation();

                    mMap.setMyLocationEnabled(true);
                    //Disable default location button
                   // mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
        });
    }


    //3. Get Device  Location
    public void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: Getting current device location");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try{
            if(LocationPermissionGranted){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Found Location");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException exc){
            Log.e(TAG, "getDeviceLocation: Security Exception:"+ exc.getMessage());
        }

    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: Moving the Camera to: lat: "+ latLng.latitude + " , lng: "+ latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

    }

}
