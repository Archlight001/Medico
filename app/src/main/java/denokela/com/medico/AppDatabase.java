package denokela.com.medico;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {UserEntity.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static  AppDatabase instance;
    public abstract UserDao userDao();


    //synchronized means at least one thread at a time can access the database
    public static synchronized  AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,"Medico_Database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    //Populate the database when its called the first time
    private static  RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsynctask(instance).execute();
        }
    };

    private static class PopulateDbAsynctask extends AsyncTask<Void,Void,Void>{
        private UserDao userDao;

        private  PopulateDbAsynctask(AppDatabase db){
            userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new UserEntity("UFirstName","Usurname",10)) ;
            return null;
        }
    }


}
