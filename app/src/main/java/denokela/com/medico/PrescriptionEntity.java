package denokela.com.medico;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prescription_table")
public class PrescriptionEntity {


    public PrescriptionEntity(String patientName, String drugName, String drugForm, int doseInterval, int doseNumber, int numberTaken, int count) {
        this.patientName = patientName;
        this.drugName = drugName;
        this.drugForm = drugForm;
        this.doseInterval = doseInterval;
        this.doseNumber = doseNumber;
        this.numberTaken = numberTaken;
        this.count = count;
    }

    @PrimaryKey(autoGenerate = true)
    int Prescription_Id;

    @ColumnInfo(name = "PatientName")
    String patientName;

    @ColumnInfo(name="DrugName")
    String drugName;

    @ColumnInfo(name ="DrugForm")
    String drugForm;

    @ColumnInfo(name="DoseInterval")
    int doseInterval;

    @ColumnInfo(name="DoseNumber")
    int doseNumber;

    @ColumnInfo(name="Number_of_Times_to_be_Taken")
    int numberTaken;

    @ColumnInfo(name="Counter")
    int count;


    public int getPrescription_Id() {
        return Prescription_Id;
    }

    public void setPrescription_Id(int prescription_Id) {
        Prescription_Id = prescription_Id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugForm() {
        return drugForm;
    }

    public void setDrugForm(String drugForm) {
        this.drugForm = drugForm;
    }

    public int getDoseInterval() {
        return doseInterval;
    }

    public void setDoseInterval(int doseInterval) {
        this.doseInterval = doseInterval;
    }

    public int getDoseNumber() {
        return doseNumber;
    }

    public void setDoseNumber(int doseNumber) {
        this.doseNumber = doseNumber;
    }


    public int getNumberTaken() {
        return numberTaken;
    }

    public void setNumberTaken(int numberTaken) {
        this.numberTaken = numberTaken;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
