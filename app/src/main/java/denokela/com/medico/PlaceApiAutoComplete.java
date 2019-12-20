package denokela.com.medico;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceApiAutoComplete {
    private static final String TAG = "PlaceApiAutoComplete";
        public ArrayList<String> autoComplete(String input){
            ArrayList<String> arrayList=new ArrayList();
            HttpURLConnection connection=null;
            StringBuilder jsonResult=new StringBuilder();
            try {
                Log.d(TAG, "autoComplete: Retrieving locations");
                StringBuilder sb=new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
                sb.append("input="+input);
                sb.append("&country=Nigeria");
                sb.append("&key=AIzaSyD3TzlzxMaX96FC5kB8etS6Uf4h9u8QkQI");
                Log.d(TAG, "autoComplete: "+sb.toString());
                URL url=new URL(sb.toString());
                connection=(HttpURLConnection)url.openConnection();
                InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream());

                int read;

                char[] buff=new char[1024];
                while ((read=inputStreamReader.read(buff))!=-1){
                    jsonResult.append(buff,0,read);
                }
                Log.d(TAG, "autoComplete: "+jsonResult);
            }
            catch (MalformedURLException e){
                e.printStackTrace();
                Log.e(TAG, "autoComplete: ", e );
            }
            catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, "autoComplete: ",e );
            }
            finally {
                if(connection!=null){
                    connection.disconnect();
                }
            }

            try {
                JSONObject jsonObject=new JSONObject(jsonResult.toString());
                JSONArray prediction=jsonObject.getJSONArray("predictions");
                for(int i=0;i<prediction.length();i++){
                    arrayList.add(prediction.getJSONObject(i).getString("description"));
                }
            }
            catch (JSONException e){
                e.printStackTrace();
                Log.e(TAG, "autoComplete: ", e);
            }

            return arrayList;
        }

}
