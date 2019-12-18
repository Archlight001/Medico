package denokela.com.medico.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import denokela.com.medico.entities.DoctorsEntity;
import denokela.com.medico.repositories.DoctorsRepository;

public class DoctorsViewModel extends AndroidViewModel {

    public DoctorsRepository doctorsRepository;

    public LiveData<List<DoctorsEntity>> getAllDoctors;
    public LiveData<List<DoctorsEntity>> getSpecificDoctors;

    public DoctorsViewModel(@NonNull Application application) {
        super(application);
        doctorsRepository = new DoctorsRepository(application);
        getAllDoctors = doctorsRepository.getAllDoctors();
    }

    public LiveData<List<DoctorsEntity>> getAllDoctors(){
        return getAllDoctors;
    }

    public LiveData<List<DoctorsEntity>> getSpecificDoctors(String Specialty){
        return doctorsRepository.getSpecificDoctors(Specialty);
    }
}
