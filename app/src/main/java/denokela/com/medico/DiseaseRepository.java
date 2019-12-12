package denokela.com.medico;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DiseaseRepository {

    private DiseaseDao diseaseDao;
    private DiseaseSymptomsDao diseaseSymptomsDao;
    private LiveData<List<DiseaseSymptomsEntity>> gastroSymptoms;
    private LiveData<List<DiseaseSymptomsEntity>> choleraSymptoms;
    private LiveData<List<DiseaseSymptomsEntity>> typhoidSymptoms;
    private LiveData<List<DiseaseSymptomsEntity>> meningitisSymptoms;
    private LiveData<List<DiseaseSymptomsEntity>> renalFailureSymptoms;

    public DiseaseRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        diseaseDao = appDatabase.diseaseDao();
        diseaseSymptomsDao = appDatabase.diseaseSymptomsDao();

        gastroSymptoms = diseaseSymptomsDao.getGastroSymptoms();
        choleraSymptoms = diseaseSymptomsDao.getCholeraSymptoms();
        typhoidSymptoms = diseaseSymptomsDao.getTyphoidSymptoms();
        meningitisSymptoms = diseaseSymptomsDao.getMeningitisSymptoms();
        renalFailureSymptoms = diseaseSymptomsDao.getRenalFailureSymptoms();

    }


    public LiveData<List<DiseaseEntity>> getDiseases(String diseaseName){
        return diseaseDao.getDiseases(diseaseName);
    }



    public LiveData<List<DiseaseSymptomsEntity>> getGastroSymptoms(){
        return gastroSymptoms;
    }


    public LiveData<List<DiseaseSymptomsEntity>> getCholeraSymptoms(){
        return choleraSymptoms;
    }


    public LiveData<List<DiseaseSymptomsEntity>> getTyphoidSymptoms(){
        return typhoidSymptoms;
    }


    public LiveData<List<DiseaseSymptomsEntity>> getMeningitisSymptoms(){
        return meningitisSymptoms;
    }


    public LiveData<List<DiseaseSymptomsEntity>> getRenalFailureSymptoms(){
        return renalFailureSymptoms;
    }
}
