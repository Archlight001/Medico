package denokela.com.medico;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static  AppDatabase INSTANCE;
    public abstract UserDao userDao();

//    public AppDatabase getAppdatabase(Context context){
//        if(INSTANCE == null){
//            INSTANCE = Room.databaseBuilder(context.getApplicationContext())
//        }
//    }
}
