package denokela.com.medico.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Disease Symptoms")
public class DiseaseSymptomsEntity {

    public DiseaseSymptomsEntity(String Gastroenteritis, String Cholera, String Typhoid, String Meningitis, String RenalFailure) {
        this.Gastroenteritis = Gastroenteritis;
        this.Cholera = Cholera;
        this.Typhoid = Typhoid;
        this.Meningitis = Meningitis;
        this.RenalFailure = RenalFailure;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;


    private String Gastroenteritis;

    private String Cholera;

    private String Typhoid;

    private String Meningitis;

    private String RenalFailure;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGastroenteritis() {
        return Gastroenteritis;
    }

    public void setGastroenteritis(String gastroenteritis) {
        Gastroenteritis = gastroenteritis;
    }

    public String getCholera() {
        return Cholera;
    }

    public void setCholera(String cholera) {
        Cholera = cholera;
    }

    public String getTyphoid() {
        return Typhoid;
    }

    public void setTyphoid(String typhoid) {
        Typhoid = typhoid;
    }

    public String getMeningitis() {
        return Meningitis;
    }

    public void setMeningitis(String meningitis) {
        Meningitis = meningitis;
    }

    public String getRenalFailure() {
        return RenalFailure;
    }

    public void setRenalFailure(String renalFailure) {
        RenalFailure = renalFailure;
    }
}
