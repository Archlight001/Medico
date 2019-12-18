package denokela.com.medico.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import denokela.com.medico.AppDatabase;
import denokela.com.medico.dao.DoctorsDao;
import denokela.com.medico.entities.DoctorsEntity;

public class DoctorsRepository {

    public DoctorsDao doctorsDao;

    public LiveData<List<DoctorsEntity>> getAllDoctors;
    public LiveData<List<DoctorsEntity>> getSpecificDoctors;

    public DoctorsRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        doctorsDao = appDatabase.doctorsDao();
        getAllDoctors = doctorsDao.getAllDoctors();
    }

    public LiveData<List<DoctorsEntity>> getAllDoctors(){
        return getAllDoctors;
    }

    public LiveData<List<DoctorsEntity>> getSpecificDoctors(String Specialty){
        return doctorsDao.getDoctors(Specialty);
    }
}
