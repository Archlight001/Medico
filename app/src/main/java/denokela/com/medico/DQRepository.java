package denokela.com.medico;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DQRepository {

    private DQSimilarDao dqSimilarDao;
    private  DQDao dqDao;
    private LiveData<List<DQSimilarEntity>> similarquestions;
    private LiveData<List<DQEntity>> diagnosisquestions;

    public DQRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        dqSimilarDao = appDatabase.dqSimilarDao();
        dqDao = appDatabase.dqDao();
        similarquestions = dqSimilarDao.getSimilarQuestions();
        diagnosisquestions = dqDao.getDiagnosisQuestions();
    }


    public LiveData<List<DQSimilarEntity>> getAllSimilarQuestions(){
        return similarquestions;
    }

    public LiveData<List<DQEntity>> getAllDiagnosisQuestions() {return  diagnosisquestions; }
}
