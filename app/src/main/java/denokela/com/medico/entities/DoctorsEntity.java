package denokela.com.medico.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Doctor Table")
public class DoctorsEntity {

    public DoctorsEntity(String doctorName, String doctorAddress, String doctorsEmail, String doctorsPhoneNumber, String doctorsSpecialty) {
        this.doctorName = doctorName;
        this.doctorAddress = doctorAddress;
        this.doctorsEmail = doctorsEmail;
        this.doctorsPhoneNumber = doctorsPhoneNumber;
        this.doctorsSpecialty = doctorsSpecialty;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Name")
    private String doctorName;

    @ColumnInfo(name = "Address")
    private String doctorAddress;

    @ColumnInfo(name = "Email Address")
    private String doctorsEmail;

    @ColumnInfo(name = "Phone Number")
    private String doctorsPhoneNumber;

    @ColumnInfo(name = "Specialty")
    private String doctorsSpecialty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public String getDoctorsEmail() {
        return doctorsEmail;
    }

    public void setDoctorsEmail(String doctorsEmail) {
        this.doctorsEmail = doctorsEmail;
    }

    public String getDoctorsPhoneNumber() {
        return doctorsPhoneNumber;
    }

    public void setDoctorsPhoneNumber(String doctorsPhoneNumber) {
        this.doctorsPhoneNumber = doctorsPhoneNumber;
    }

    public String getDoctorsSpecialty() {
        return doctorsSpecialty;
    }

    public void setDoctorsSpecialty(String doctorsSpecialty) {
        this.doctorsSpecialty = doctorsSpecialty;
    }
}
