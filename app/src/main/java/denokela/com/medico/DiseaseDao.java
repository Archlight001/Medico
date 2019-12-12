package denokela.com.medico;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiseaseDao {

    @Insert
    void insert(DiseaseEntity diseaseEntity);

    @Query("SELECT * FROM Diseases WHERE Name =:disease")
    LiveData<List<DiseaseEntity>> getDiseases(String disease);
}
