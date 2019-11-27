package denokela.com.medico;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "similar_diagnosis_questions_table")
public class DQSimilarEntity {

    public DQSimilarEntity(String SDQ) {
        this.SDQ = SDQ;
    }

    @PrimaryKey(autoGenerate = true)
    private int SQID;

    @ColumnInfo(name = "Diagnosis_Questions")
    private String SDQ;

    public int getSQID() {
        return SQID;
    }

    public void setSQID(int SQID) {
        this.SQID = SQID;
    }

    public String getSDQ() {
        return SDQ;
    }

    public void setSDQ(String SDQ) {
        this.SDQ = SDQ;
    }

    //SDQ - Similar Diagnostic Questions
}
