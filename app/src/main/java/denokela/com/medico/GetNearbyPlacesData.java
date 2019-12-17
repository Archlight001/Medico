package denokela.com.medico;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> {
    String googlePlacesdata;
    GoogleMap googleMap;
    String url;
    Context context;
    private static final String TAG = "GetNearbyPlacesData";

    public GetNearbyPlacesData(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Object... objects) {
        Log.d(TAG, "doInBackground: Getting object data");
        googleMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesdata = downloadUrl.readUrl(url);
            Log.d(TAG, "doInBackground: Read url data");
            Log.d(TAG, "doInBackground: "+googlePlacesdata);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: ",e );
        }
        return googlePlacesdata;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: "+s);
        if(s.contains("ZERO_RESULTS")){
            Toast.makeText(context, "No Place Found", Toast.LENGTH_SHORT).show();
        }else if(s.contains("OVER_QUERY_LIMIT")){
            Toast.makeText(context, "Can't Make Searches, Query Limit reached", Toast.LENGTH_SHORT).show();
        }else{
            List<HashMap<String,String>> nearbyPlaceList=null;
            DataParser dataParser = new DataParser();
            nearbyPlaceList = dataParser.parse(s);
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

            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        }

    }
}
