package denokela.com.medico;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class PrescriptionViewModel extends AndroidViewModel {

    private PrescriptionRepository repository;
    private LiveData<List<PrescriptionEntity>> allPrescriptions;


    public PrescriptionViewModel(@NonNull Application application) {
        super(application);
        repository = new PrescriptionRepository(application);
        allPrescriptions = repository.getAllPrescriptions();
    }
    public void insert(PrescriptionEntity prescriptionEntity){
        repository.insert(prescriptionEntity);
    }
    public void update(PrescriptionEntity prescriptionEntity){
        repository.update(prescriptionEntity);
    }
    public void delete(PrescriptionEntity prescriptionEntity){
        repository.delete(prescriptionEntity);
    }


    public void deleteAllNotes(){
        repository.deleteAllPrescriptions();
    }
    public LiveData<List<PrescriptionEntity>> getAllPrescriptions(){
        return  allPrescriptions;
    }

    public LiveData<List<PrescriptionEntity>> getCertainPrescription(Integer value){
        return repository.getPrescriptions(value);
    }
    public LiveData<List<PrescriptionEntity>> getPrescriptionPatientDrug(Integer patID, String DrugName){
        return repository.getPrescriptionPatientDrug(patID, DrugName);
    }



}
