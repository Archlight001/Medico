package denokela.com.medico.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import denokela.com.medico.entities.PrescriptionEntity;

@Dao
public interface PrescriptionDao {

    @Insert
    void insert(PrescriptionEntity prescriptionEntity);

    @Update
    void update(PrescriptionEntity prescriptionEntity);

    @Delete
    void delete(PrescriptionEntity prescriptionEntity);

    @Query("DELETE FROM prescription_table")
    void deleteEveryPrescription();

    @Query("DELETE FROM prescription_table WHERE PatientID=:value")
    void deleteAllPrescriptions(int value);

    @Query("SELECT * FROM prescription_table")
    LiveData<List<PrescriptionEntity>> getAllPrescriptions();

    @Query("SELECT * FROM prescription_table WHERE PatientID= :value OR Prescription_Id=:value")
            LiveData<List<PrescriptionEntity>> getCertainPrescription(int value);

    @Query("SELECT * FROM prescription_table WHERE PatientID= :value AND DrugName= :drugNameValue")
    List<PrescriptionEntity> getPrescriptionPatientDrug(int value, String drugNameValue);

    @Query("UPDATE prescription_table SET Counter =:count WHERE Prescription_Id=:id")
    void updateCount(Integer count, Integer id);

}
