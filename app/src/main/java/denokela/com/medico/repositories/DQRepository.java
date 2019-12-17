package denokela.com.medico.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import denokela.com.medico.AppDatabase;
import denokela.com.medico.dao.DQDao;
import denokela.com.medico.entities.DQEntity;

public class DQRepository {

    public DQDao dqDao;
    private LiveData<List<DQEntity>> diagnosisquestions;

    public DQRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        dqDao = appDatabase.dqDao();
        diagnosisquestions = dqDao.getDiagnosisQuestions();
    }



    public LiveData<List<DQEntity>> getAllDiagnosisQuestions() {return  diagnosisquestions; }
}
