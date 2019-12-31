package denokela.com.medico.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import denokela.com.medico.entities.PrescriptionEntity;
import denokela.com.medico.repositories.PrescriptionRepository;

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
    public void deleteEveryPrescription(){
        repository.deleteEveryPrescription();
    }

    public void deleteAllPrescriptions(Integer patientId){
        repository.deleteAllPrescriptions(patientId);
    }

    public void updateCount(Integer count,Integer id){
        repository.updateCount(count,id);
    }
    public LiveData<List<PrescriptionEntity>> getAllPrescriptions(){
        return  allPrescriptions;
    }

    public LiveData<List<PrescriptionEntity>> getCertainPrescription(Integer value){
        return repository.getPrescriptions(value);
    }
    public List<PrescriptionEntity> getPrescriptionPatientDrug(Integer patID, String DrugName){
        return repository.getPrescriptionPatientDrug(patID, DrugName);
    }



}
