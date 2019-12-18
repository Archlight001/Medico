package denokela.com.medico;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import denokela.com.medico.dao.DQDao;
import denokela.com.medico.dao.DiseaseDao;
import denokela.com.medico.dao.DiseaseSymptomsDao;
import denokela.com.medico.dao.DoctorsDao;
import denokela.com.medico.dao.PrescriptionDao;
import denokela.com.medico.dao.UserDao;
import denokela.com.medico.entities.DQEntity;
import denokela.com.medico.entities.DiseaseEntity;
import denokela.com.medico.entities.DiseaseSymptomsEntity;
import denokela.com.medico.entities.DoctorsEntity;
import denokela.com.medico.entities.PrescriptionEntity;
import denokela.com.medico.entities.UserEntity;

@Database(entities = {UserEntity.class, PrescriptionEntity.class,
        DQEntity.class, DiseaseEntity.class,DiseaseSymptomsEntity.class, DoctorsEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();

    public abstract PrescriptionDao prescriptionDao();


    public abstract DQDao dqDao();

    public abstract DiseaseDao diseaseDao();

    public abstract DiseaseSymptomsDao diseaseSymptomsDao();

    public abstract DoctorsDao doctorsDao();


    //synchronized means at least one thread at a time can access the database
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "Medico_Database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    //Populate the database when its called the first time
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsynctask(instance).execute();
        }
    };

    private static class PopulateDbAsynctask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;
        private PrescriptionDao prescriptionDao;
        private DQDao dqDao;
        private DiseaseDao diseaseDao;
        private DiseaseSymptomsDao diseaseSymptomsDao;
        private DoctorsDao doctorsDao;

        private PopulateDbAsynctask(AppDatabase db) {
            userDao = db.userDao();
            prescriptionDao = db.prescriptionDao();
            dqDao = db.dqDao();
            diseaseDao = db.diseaseDao();
            diseaseSymptomsDao=db.diseaseSymptomsDao();
            doctorsDao = db.doctorsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //userDao.insert(new UserEntity("UFirstName", "Usurname", 10));
            prescriptionDao.insert(new PrescriptionEntity(1, "Ampiclox", "Tablet", 4, 2, 10, 1));
            prescriptionDao.insert(new PrescriptionEntity(2, "Wrythromycin", "Syrup", 4, 2, 10, 1));


            dqDao.insert(new DQEntity("Are you having Bloody stool or Dark/Brown Color Urine","Gastroenteritis"));
            dqDao.insert(new DQEntity("Are you having aches in your joints or Muscles","Gastroenteritis"));
            dqDao.insert(new DQEntity("Do you possess the Type O blood type ?", "Cholera"));
            dqDao.insert(new DQEntity("Is any of your household members having any of these symptoms ?", "Cholera"));
            dqDao.insert(new DQEntity("Do you have a strange rash on any part of your body","Typhoid"));
            dqDao.insert(new DQEntity("Do you have a poor Appetite","Typhoid"));
            dqDao.insert(new DQEntity("Are you having a lack of interest in performing activities","Meningitis"));
            dqDao.insert(new DQEntity("Are you sensitive to Bright Light","Meningitis"));
            dqDao.insert(new DQEntity("Are you experiencing reduced amounts in your urine","Renal Failure"));
            dqDao.insert(new DQEntity("Do you take alcohol ?", "Renal Failure"));


            diseaseDao.insert(new DiseaseEntity("CHOLERA"
                    , "Cholera is a serious bacterial disease that usually causes severe diarrhea and dehydration." +
                    " The disease is typically spread through contaminated water. " +
                    "Uncooked fruits, vegetables, and other foods can also contain the bacteria that cause cholera.",
                    "Diarrhea, Nausea, Vomiting, Mild to sever dehydration. " +
                            "In children however some of the extra symptoms that may occur include sever drowsiness, fever, convulsions, coma."
                    , "Steady intake of oral rehydration salts is recommended until a proper  medication has been prescribed by a physician."
                    , "When showing symptoms of cholera it is best to contact a health center where the disease can be confirmed by identifying bacteria in a stool sample."));

            diseaseDao.insert(new DiseaseEntity("GASTROENTERITIS"
                    , "Viral gastroenteritis is an inflammation of your stomach and intestines caused by one of any number of viruses." +
                    " Also known as the stomach flu, it  spreads through close contact with people who are infected or through contaminated food or water."
                    , "Diarrhea, Nausea, Vomiting, Headache, Muscle aches or joint aches, Fever, Sweating, Abdominal cramps and pain, loss of appetite. These symptoms can last anywhere from 1-10 days. " +
                    "QUICKLY get to the nearest health center if your diarrhea has lasted for three days without getting less frequent or the presence of blood in your diarrhea or you’re showing signs of dehydration such as dry lips or dizziness."
                    , "Prevention of dehydration by drinking lots of fluids  and getting lots of rest before efficient treatment has been prescribed"
                    , "Most of the time, a medical history and physical exam is the basis for diagnosis, especially if there’s evidence that the virus is spreading through your community. " +
                    "You may also visit a nearby health center to order a stool sample to test for the type of virus or to find out if your illness is caused by a parasitic or bacterial infection."));

            diseaseDao.insert(new DiseaseEntity("TYPHOID FEVER"
                    , "Typhoid fever is a serious bacterial infection that easily spreads through contaminated water and food. With treatment, most people make a full recovery. " +
                    "But untreated typhoid can lead to life-threatening complications."
                    , "High fever, weakness, stomach pain, headache, poor appetite, rash, fatigue, confusion, constipation and diarrhea. With more severe symptoms being pneumonia, pancreatis, kidney or bladder infection and meningitis"
                    , "If percent of diagnosis is over 50% it is best to avoid other people, wash your hands often and don’t prepare or serve food until proper tests and proper treatment has been administered."
                    , "A blood test can confirm the presence of S. typhi. Typhoid is treated with antibiotics."));

            diseaseDao.insert(new DiseaseEntity("MENINGITIS"
                    , "Meningitis is an inflammation of the meninges. The meninges are the three membranes that cover the brain and spinal cord. Meningitis can occur when fluid surrounding the meninges becomes infected. " +
                    "The most common causes of meningitis are viral and bacterial infections, others may include cancer, chemical irritation, fungi and drug allergies"
                    , "\tViral – In Infants (Decreased appetite, irritability, sleepiness, lethargy, fever), In Adults (Headaches, fever, stiff neck, seizures, sensitivity to bright light, sleepiness, nausea, vomiting, decreased appetite)\n" +
                    "\n" +
                    "\tBacterial – Altered Mental status, nausea, vomiting, sensitivity to light, irritability, headache, fever, chills, stiff neck, sleepiness, skin rash\n" +
                    "\n" +
                    "\tFungal – Nausea, vomiting, sensitivity to light, fever, headache, confusion"
                    , "Seek immediate medical attention if you experience symptoms of Bacterial and viral meningitis as they can be deadly."
                    , "Early diagnosis and treatment will prevent brain damage and death. Bacterial meningitis is treated with intravenous antibiotics. Fungal meningitis is treated with antifungal agents. " +
                    "Viral meningitis may resolve on its own, but some causes of viral meningitis will be treated with intravenous antiviral medications."));

            diseaseDao.insert(new DiseaseEntity("RENAL FAILURE"
                    , "Kidney failure occurs when your kidneys lose the ability to sufficiently filter waste from your blood."
                    , "Reduced amount of urine, swelling of your legs, ankles and feet, shortness of breath, fatigue, nausea, pain or pressures in the chest, seizures, coma."
                    , "Limit your intake of sodium, potassium and phosphorus if you’re experiencing any of the symptoms. And also look to visit any health c    enter for a test on Urinalysis, " +
                    "Blood samples, Kidney tissue samples or Imaging for a more efficient and accurate diagnosis"
                    , "An important information is to note that Diabetes is the most common cause of kidney failure. Uncontrolled high blood sugar can damage kidneys. The damage can become worse over time. "));

            diseaseSymptomsDao.insert(new DiseaseSymptomsEntity("Diarrhea","Diarrhea","Fever","Decreased Appetite","Swelling Limbs"));
            diseaseSymptomsDao.insert(new DiseaseSymptomsEntity("Headache","Dehydration","Stomach Pain","Headache","Fatigue"));
            diseaseSymptomsDao.insert(new DiseaseSymptomsEntity("Abdominal Cramps","Fever","Fatigue","Nausea","Nausea"));
            diseaseSymptomsDao.insert(new DiseaseSymptomsEntity("Decreased Appetite","Nausea","Diarrhea","Stiff Neck","Chest Pain"));
            diseaseSymptomsDao.insert(new DiseaseSymptomsEntity("Nausea","Vomiting","Headache","Fever",""));
            diseaseSymptomsDao.insert(new DiseaseSymptomsEntity("Vomiting","","Extreme Cold","",""));
            diseaseSymptomsDao.insert(new DiseaseSymptomsEntity("Dry Mouth","","Nausea","",""));
            diseaseSymptomsDao.insert(new DiseaseSymptomsEntity("Dehydration","","Vomiting","",""));

            doctorsDao.insert(new DoctorsEntity("Dr. Abiola Godwin","Mabera","abiolaGodwin@gmail.com","07064411985","Gastroenteritis Cholera"));
            doctorsDao.insert(new DoctorsEntity("Dr. Elias David","Old Airport","eliasdavid242@gmail.com","08043219876","Cholera"));
            doctorsDao.insert(new DoctorsEntity("Dr Joseph Pada","Fodio Road","padajo@yahoo.com","08038378080","Typhoid Meningitis"));
            doctorsDao.insert(new DoctorsEntity("Dr Rachel Bawa","Goronyo Road","bawarachel@gmail.com","09054637219","Meningitis"));
            doctorsDao.insert(new DoctorsEntity("Dr Barak Obama","Offa Road","barakK@gmail.com","07064411984","Renal Failure"));

            return null;
        }
    }


}
