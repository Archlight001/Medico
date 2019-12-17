package denokela.com.medico;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl {

    private static final String TAG = "DownloadUrl";

    public String readUrl(String myUrl) throws IOException{
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while((line= bufferedReader.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "readUrl: ",e );
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "readUrl: ", e);
        }finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
