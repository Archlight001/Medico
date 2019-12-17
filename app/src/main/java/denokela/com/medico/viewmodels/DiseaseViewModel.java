package denokela.com.medico.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import denokela.com.medico.entities.DiseaseEntity;
import denokela.com.medico.repositories.DiseaseRepository;
import denokela.com.medico.entities.DiseaseSymptomsEntity;

public class DiseaseViewModel extends AndroidViewModel {

    private DiseaseRepository diseaseRepository;

    public DiseaseViewModel(@NonNull Application application) {
        super(application);
        diseaseRepository = new DiseaseRepository(application);
    }


    public LiveData<List<DiseaseEntity>> getDiseases(String diseaseName){
        return diseaseRepository.getDiseases(diseaseName);
    }

    public LiveData<List<DiseaseSymptomsEntity>> getGastroSymptoms(){
        return diseaseRepository.getGastroSymptoms();
    }

    public LiveData<List<DiseaseSymptomsEntity>> getCholeraSymptoms(){
        return diseaseRepository.getCholeraSymptoms();
    }

    public LiveData<List<DiseaseSymptomsEntity>> getTyphoidSymptoms(){
        return diseaseRepository.getTyphoidSymptoms();
    }

    public LiveData<List<DiseaseSymptomsEntity>> getMeningitisSymptoms(){
        return diseaseRepository.getMeningitisSymptoms();
    }

    public LiveData<List<DiseaseSymptomsEntity>> getRenalFailureSymptoms(){
        return diseaseRepository.getRenalFailureSymptoms();
    }
}
