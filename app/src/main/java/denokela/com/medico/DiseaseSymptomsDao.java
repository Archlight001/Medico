package denokela.com.medico;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiseaseSymptomsDao {

    @Insert
    void insert(DiseaseSymptomsEntity diseaseSymptomsEntity);

    @Query("SELECT `id`,`Gastroenteritis` FROM `Disease Symptoms`")
    LiveData<List<DiseaseSymptomsEntity>> getGastroSymptoms();

    @Query("SELECT `id`,`Cholera` FROM `Disease Symptoms`")
    LiveData<List<DiseaseSymptomsEntity>> getCholeraSymptoms();

    @Query("SELECT `id`,`Typhoid` FROM `Disease Symptoms`")
    LiveData<List<DiseaseSymptomsEntity>> getTyphoidSymptoms();

    @Query("SELECT `id`,`Meningitis` FROM `Disease Symptoms`")
    LiveData<List<DiseaseSymptomsEntity>> getMeningitisSymptoms();

    @Query("SELECT `id`,`RenalFailure` FROM `Disease Symptoms`")
    LiveData<List<DiseaseSymptomsEntity>> getRenalFailureSymptoms();

}
