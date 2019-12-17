package denokela.com.medico.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import denokela.com.medico.entities.DQEntity;

@Dao
public interface DQDao {

    @Insert
    void insert(DQEntity dqEntity);

    @Query("SELECT * FROM Diagnosis_questions_table")
    LiveData<List<DQEntity>> getDiagnosisQuestions();
}
