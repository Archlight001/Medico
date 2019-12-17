package denokela.com.medico.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import denokela.com.medico.entities.DiseaseEntity;

@Dao
public interface DiseaseDao {

    @Insert
    void insert(DiseaseEntity diseaseEntity);

    @Query("SELECT * FROM Diseases WHERE Name =:disease")
    LiveData<List<DiseaseEntity>> getDiseases(String disease);
}
