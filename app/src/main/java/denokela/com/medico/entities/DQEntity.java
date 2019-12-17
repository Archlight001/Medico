package denokela.com.medico.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Diagnosis_questions_table")
public class DQEntity {

    public DQEntity(String DQ, String Disease) {
        this.DQ = DQ;
        this.Disease = Disease;
    }

    @PrimaryKey(autoGenerate = true)
    private int DQID;

    @ColumnInfo(name = "Diagnosis_Question")
    private String DQ;

    @ColumnInfo(name = "Disease")
    private String Disease;

    public int getDQID() {
        return DQID;
    }

    public void setDQID(int DQID) {
        this.DQID = DQID;
    }

    public String getDQ() {
        return DQ;
    }

    public void setDQ(String DQ) {
        this.DQ = DQ;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }
}
