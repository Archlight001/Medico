package denokela.com.medico;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DQSimilarDao {

    @Insert
    void insert(DQSimilarEntity dqSimilarEntity);

    @Query("SELECT * FROM similar_diagnosis_questions_table")
    LiveData<List<DQSimilarEntity>> getSimilarQuestions();
}
