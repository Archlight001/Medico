package denokela.com.medico;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {UserEntity.class,PrescriptionEntity.class,DQSimilarEntity.class,DQEntity.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static  AppDatabase instance;
    public abstract UserDao userDao();
    public abstract PrescriptionDao prescriptionDao();
    public abstract DQSimilarDao dqSimilarDao();
    public abstract DQDao dqDao();


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
        private PrescriptionDao prescriptionDao;
        private DQSimilarDao dqSimilarDao;
        private DQDao dqDao;

        private  PopulateDbAsynctask(AppDatabase db){
            userDao = db.userDao();
            prescriptionDao = db.prescriptionDao();
            dqSimilarDao = db.dqSimilarDao();
            dqDao = db.dqDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new UserEntity("UFirstName","Usurname",10)) ;
            prescriptionDao.insert(new PrescriptionEntity(1,"Ampiclox","Tablet",4,2,10,1));
            prescriptionDao.insert(new PrescriptionEntity(2,"Wrythromycin","Syrup",4,2,10,1));

            dqSimilarDao.insert(new DQSimilarEntity("Are you having a Fever ?"));
            dqSimilarDao.insert(new DQSimilarEntity("Are you experiencing Nausea or Vomiting ?"));
            dqSimilarDao.insert(new DQSimilarEntity("Any sign of Muscle or Normal Weakness ?"));
            dqSimilarDao.insert(new DQSimilarEntity("Are you experiencing series of Diarrhea ?"));
            dqSimilarDao.insert(new DQSimilarEntity("Are you feeling Dehydrated ?"));


            dqDao.insert(new DQEntity("Are you having pains in your abdomen or stomach ?","Gastroenteritis"));
            dqDao.insert(new DQEntity("Are you having Bloody stool or Dark/Brown Color Urine ?","Gastroenteritis"));
            dqDao.insert(new DQEntity("Do you possess the Type O blood type ?","Cholera"));
            dqDao.insert(new DQEntity("Is any of your household members having any of these symptoms ?","Cholera"));
            dqDao.insert(new DQEntity("Are you feeling severe cold ?","Typhoid"));
            dqDao.insert(new DQEntity("Do you have a strange rash on any part of your body ?","Typhoid"));
            dqDao.insert(new DQEntity("Do you have a Headache ?","Typhoid"));
            dqDao.insert(new DQEntity("Do you have a poor Appetite ?","Typhoid"));
            dqDao.insert(new DQEntity("Are you having a stiff neck ?","Meningitis"));
            dqDao.insert(new DQEntity("Are you sensitive to bright light ?","Meningitis"));
            dqDao.insert(new DQEntity("Are you having a lack of interest in performing activities ?","Meningitis"));
            dqDao.insert(new DQEntity("Are you having shortness of breath ?","Renal Failure"));
            dqDao.insert(new DQEntity("Do you have pain or pressure in your chest ?","Renal Failure"));
            dqDao.insert(new DQEntity("Are you experiencing reduced amounts in your urine ?","Renal Failure"));
            dqDao.insert(new DQEntity("Do you take alcohol ?","Renal Failure"));




            return null;
        }
    }


}
