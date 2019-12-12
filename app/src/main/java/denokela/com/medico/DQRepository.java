package denokela.com.medico;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DQRepository {

    private  DQDao dqDao;
    private LiveData<List<DQEntity>> diagnosisquestions;

    public DQRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        dqDao = appDatabase.dqDao();
        diagnosisquestions = dqDao.getDiagnosisQuestions();
    }



    public LiveData<List<DQEntity>> getAllDiagnosisQuestions() {return  diagnosisquestions; }
}
