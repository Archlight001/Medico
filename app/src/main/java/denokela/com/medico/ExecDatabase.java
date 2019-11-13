package denokela.com.medico;

import android.content.Context;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExecDatabase extends AsyncTask<String,Void,List> {

    AppDatabase db;
    List<User> dresult = null;
    List<String> result= new ArrayList<>();
    private static final String TAG = "ExecDatabase";
    Context context;

    public interface AsyncRespone{
        void processfinish(List output);
    }

    String connecturl;
    public AsyncRespone delegate = null;

    public ExecDatabase(AsyncRespone delegate, String connecturl, Context context) {
        this.delegate = delegate;
        this.connecturl = connecturl;
        this.context = context;
    }


    @Override
    protected List doInBackground(String... params) {
        db= Room.databaseBuilder(context,AppDatabase.class,"Medico_Database")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        if(connecturl.equals("insertData")){
            String fName = params[0];
            String sName = params[1];
            String age = params[2];
            User inputs = new User(fName,sName,age);
            db.userDao().insertAll(inputs);
            result.add("Success");
            Log.d(TAG, "Inserted Successfully");
            return result;
        }else if(connecturl.equals("readData")){

                dresult= db.userDao().getAllResults();

            return dresult;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List list) {
        delegate.processfinish(list);
    }
}
