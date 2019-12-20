package denokela.com.medico.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import denokela.com.medico.DataParser;
import denokela.com.medico.GetNearbyPlacesData;
import denokela.com.medico.R;
import denokela.com.medico.activities.DoctorsList;
import denokela.com.medico.activities.MainActivity;
import denokela.com.medico.activities.UserReg;

public class HealthCenterMaps extends Fragment implements GoogleMap.OnInfoWindowClickListener, GetNearbyPlacesData.AsyncResponse {

    private static final String TAG = "HealthCenterMaps";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final float DEFAULT_ZOOM = 15f;
    private static int ERROR_DIALOG_REQUEST = 998;
    private Boolean LocationPermissionGranted = false;

    private FusedLocationProviderClient fusedLocationProviderClient;

    GoogleMap mMap;

    Spinner spinnerLocations;

    ImageView iv_myLocation,iv_doctor;

    GeoApiContext mGeoApiContext = null;
    double latitude,longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_health_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerLocations = view.findViewById(R.id.spinner_location_parameters);
        iv_myLocation = view.findViewById(R.id.image_view_btn_myLocation);
        iv_doctor = view.findViewById(R.id.image_doctor);

        String [] locations = {"Select a Health Location","Hospitals","Clinics","Pharmacies"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,locations);
        spinnerLocations.setAdapter(arrayAdapter);

        if (isServicesOk()) {
            getLocationPermission();
        }



    }


    private void init(){
        Log.d(TAG, "init: initializing");
        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.Google_Directions_Key))
                    .build();

        }
        spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //When spinner item is clicked geolocate to locations around it
                if(spinnerLocations.getSelectedItem().toString().equals("Select a Health Location")){
                    Toast.makeText(getActivity(), "Select a Health Location", Toast.LENGTH_SHORT).show();
                }else{
                    geolocate();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        iv_myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });

        iv_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select a Disease");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.select_dialog_singlechoice);
                    arrayAdapter.add("Gastroenteritis");
                    arrayAdapter.add("Cholera");
                    arrayAdapter.add("Typhoid");
                    arrayAdapter.add("Meningitis");
                    arrayAdapter.add("Renal Failure");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strname = arrayAdapter.getItem(i);
                        AlertDialog.Builder builderinner = new AlertDialog.Builder(getContext());
                        builderinner.setTitle("Disease Selected is: ");
                        builderinner.setMessage(strname);
                        builderinner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getActivity(), DoctorsList.class);
                                intent.putExtra("DiseaseName",strname);
                                startActivity(intent);
                            }
                        });
                        builderinner.show();
                    }
                });
                builder.show();
            }
        });

    }



    private void geolocate(){
        Log.d(TAG, "geolocate: geolocating");
        String searchString = spinnerLocations.getSelectedItem().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
//        try{
//            list = geocoder.getFromLocationName("Abuja",1);
//        }catch(IOException e){
//            Log.e(TAG, "geolocate: IOException: " + e.getMessage());
//        }
//
//        if(list.size()>0){
//            Address address = list.get(0);
//            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
//
//            Log.d(TAG, "geolocate: "+address.toString());
//        }else{
//            Toast.makeText(getActivity(),"No location found", Toast.LENGTH_SHORT).show();
//        }

        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(this);

        switch (searchString.trim()){
            case "Hospitals":
               mMap.clear();
               String Hospital = "hospital";
               String keyword = "hospital";
               String url = getUrl(latitude,longitude,Hospital,keyword);

               dataTransfer[0]=mMap;
               dataTransfer[1]=url;

               getNearbyPlacesData.execute(dataTransfer);

                Toast.makeText(getActivity(), "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
                break;
            case "Clinics":
                mMap.clear();

                url = getUrl(latitude,longitude,"hospital","UDUS clinic");
                dataTransfer[0]=mMap;
                dataTransfer[1]=url;

                getNearbyPlacesData.execute(dataTransfer);

                Toast.makeText(getActivity(), "Showing Nearby Clinics", Toast.LENGTH_LONG).show();
                break;
            case "Pharmacies":
                mMap.clear();
                String Pharmacies = "pharmacy";
                url = getUrl(latitude,longitude,"pharmacy","pharmacy");
                dataTransfer[0]=mMap;
                dataTransfer[1]=url;

                getNearbyPlacesData.execute(dataTransfer);

                Toast.makeText(getActivity(), "Showing Nearby Pharmacies", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private String getUrl(double latitude, double longitude,String nearbyPlace,String keyword){
        String googlePlaceUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=10000&type="+nearbyPlace+"&keyword="+keyword+"&key="+getString(R.string.Google_Places_Key);
        Log.d(TAG, "getUrl: "+ googlePlaceUrl);
        return googlePlaceUrl;
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
                   mMap.getUiSettings().setMyLocationButtonEnabled(false);
                   init();
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
                            latitude = currentLocation.getLatitude();
                            longitude = currentLocation.getLongitude();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM, "My Location");

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

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: Moving the Camera to: lat: "+ latLng.latitude + " , lng: "+ latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);

        mMap.addMarker(options);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(marker.getSnippet())
                .setCancelable(true)
                .setMessage("Do you want to see the route to this location")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        calculateDirections(marker);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void processfinish(String result) {
        if(result.contains("ZERO_RESULTS")){
            Toast.makeText(getActivity(), "No Place Found", Toast.LENGTH_SHORT).show();
        }else if(result.contains("OVER_QUERY_LIMIT")){
            Toast.makeText(getActivity(), "Can't Make Searches, Query Limit reached", Toast.LENGTH_SHORT).show();
        }else{
            List<HashMap<String,String>> nearbyPlaceList=null;
            DataParser dataParser = new DataParser();
            nearbyPlaceList = dataParser.parse(result);
            showNearbyPlaces(nearbyPlaceList);
        }
    }

    private void showNearbyPlaces(List<HashMap<String,String>> nearbyPlacesList){

        for(int i=0;i<nearbyPlacesList.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googlePlace = nearbyPlacesList.get(i);

            String placename = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            Log.d(TAG, "showNearbyPlaces: "+placename);
            LatLng latLng = new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(placename + " "+vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            markerOptions.snippet("Click to see route");

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12f));
            mMap.setOnInfoWindowClickListener(this);
        }

    }


    private void calculateDirections(Marker marker){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        latitude,
                        longitude
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });
    }
}
