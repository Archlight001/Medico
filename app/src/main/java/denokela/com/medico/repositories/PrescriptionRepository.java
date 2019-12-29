package denokela.com.medico.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import denokela.com.medico.AppDatabase;
import denokela.com.medico.dao.PrescriptionDao;
import denokela.com.medico.entities.PrescriptionEntity;

public class PrescriptionRepository {

    public PrescriptionDao prescriptionDao;
    private LiveData<List<PrescriptionEntity>> allPrescriptions;
    private LiveData<List<PrescriptionEntity>> certainPrescriptions;

    public PrescriptionRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        prescriptionDao = appDatabase.prescriptionDao();
        allPrescriptions = prescriptionDao.getAllPrescriptions();
    }


    public void insert(PrescriptionEntity prescriptionEntity){
        new AsyncTasker(prescriptionDao,"insert").execute(prescriptionEntity);
    }

    public void update(PrescriptionEntity prescriptionEntity){
        new AsyncTasker(prescriptionDao,"update").execute(prescriptionEntity);
    }

    public void delete(PrescriptionEntity prescriptionEntity){
        new AsyncTasker(prescriptionDao,"delete").execute(prescriptionEntity);
    }

    public void deleteAllPrescriptions(){
        new AsyncTasker(prescriptionDao,"deleteAll").execute();
    }

    public LiveData<List<PrescriptionEntity>> getAllPrescriptions(){

        return allPrescriptions;
    }

    public LiveData<List<PrescriptionEntity>> getPrescriptions(Integer v) {
        return prescriptionDao.getCertainPrescription(v);
    }

    public List<PrescriptionEntity> getPrescriptionPatientDrug(Integer patID, String DrugName){
        return prescriptionDao.getPrescriptionPatientDrug(patID,DrugName);
    }


    private static class AsyncTasker extends android.os.AsyncTask<PrescriptionEntity,Void,Void>{
        public PrescriptionDao prescriptionDao;
        private String action;

        private AsyncTasker(PrescriptionDao prescriptionDao, String action){
            this.prescriptionDao = prescriptionDao;
            this.action = action;
        }


        @Override
        protected Void doInBackground(PrescriptionEntity... prescriptionEntities) {
            if(action.equals("insert")){
                prescriptionDao.insert(prescriptionEntities[0]);
            }else if(action.equals("update")){
                prescriptionDao.update(prescriptionEntities[0]);
            }else if(action.equals("delete")){
                prescriptionDao.delete(prescriptionEntities[0]);
            }else if(action.equals("deleteAll")){
                prescriptionDao.deleteAllPrescriptions();
            }
            return null;
        }
    }
}
