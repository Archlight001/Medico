package denokela.com.medico;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface PrescriptionDao {

    @Insert
    void insert(PrescriptionEntity prescriptionEntity);

    @Update
    void update(PrescriptionEntity prescriptionEntity);

    @Delete
    void delete(PrescriptionEntity prescriptionEntity);

    @Query("DELETE FROM prescription_table")
    void deleteAllPrescriptions();

    @Query("SELECT * FROM prescription_table")
    LiveData<List<PrescriptionEntity>> getAllPrescriptions();
}
