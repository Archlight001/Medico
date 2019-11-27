package denokela.com.medico;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DQViewModel extends AndroidViewModel {
    private DQRepository dqRepository;
    private  LiveData<List<DQEntity>> diagnosisquestions;
    private LiveData<List<DQSimilarEntity>> similarquestions;

    public DQViewModel(@NonNull Application application) {
        super(application);
        dqRepository = new DQRepository(application);
        similarquestions = dqRepository.getAllSimilarQuestions();
        diagnosisquestions = dqRepository.getAllDiagnosisQuestions();

    }


    public LiveData<List<DQSimilarEntity>> getAllSimilarQuestions(){
        return similarquestions;
    }

    public LiveData<List<DQEntity>> getAllDiagnosisQuestions(){
        return diagnosisquestions;
    }

}
