package denokela.com.medico.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import denokela.com.medico.entities.DoctorsEntity;

@Dao
public interface DoctorsDao {

    @Insert
        void insert(DoctorsEntity doctorsEntity);

    @Query("SELECT * FROM `Doctor Table`")
    LiveData<List<DoctorsEntity>> getAllDoctors();

    @Query("SELECT * FROM `Doctor Table` WHERE `Specialty` LIKE :specialty")
    LiveData<List<DoctorsEntity>> getDoctors(String specialty);
}
