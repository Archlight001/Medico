package denokela.com.medico;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object,String,String>{
    String googlePlacesdata;
    GoogleMap googleMap;
    String url;
    Context context;
    private static final String TAG = "GetNearbyPlacesData";

    public interface AsyncResponse {
        void processfinish(String result);
    }
    public AsyncResponse delegate=null;

    public GetNearbyPlacesData(AsyncResponse delegate) {
        this.delegate = delegate;
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
        delegate.processfinish(s);

    }



}
