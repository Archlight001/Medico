package denokela.com.medico.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Diseases")
public class DiseaseEntity {

    public DiseaseEntity(String diseaseName,String diseaseDefinition, String diseaseSymptoms, String diseaseRecommendation, String diseaseOtherInfo) {
        this.diseaseName = diseaseName;
        this.diseaseDefinition = diseaseDefinition;
        this.diseaseSymptoms = diseaseSymptoms;
        this.diseaseRecommendation = diseaseRecommendation;
        this.diseaseOtherInfo = diseaseOtherInfo;
    }

    @PrimaryKey(autoGenerate = true)
    private int diseaseID;


    @ColumnInfo(name = "Name")
    private String diseaseName;

    @ColumnInfo(name = "Definition")
    private String diseaseDefinition;

    @ColumnInfo(name = "Symptoms")
    private String diseaseSymptoms;

    @ColumnInfo(name = "Recommendation")
    private String diseaseRecommendation;

    @ColumnInfo(name = "Other Info")
    private String diseaseOtherInfo;

    public int getDiseaseID() {
        return diseaseID;
    }

    public void setDiseaseID(int diseaseID) {
        this.diseaseID = diseaseID;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseDefinition() {
        return diseaseDefinition;
    }

    public void setDiseaseDefinition(String diseaseDefinition) {
        this.diseaseDefinition = diseaseDefinition;
    }

    public String getDiseaseSymptoms() {
        return diseaseSymptoms;
    }

    public void setDiseaseSymptoms(String diseaseSymptoms) {
        this.diseaseSymptoms = diseaseSymptoms;
    }

    public String getDiseaseRecommendation() {
        return diseaseRecommendation;
    }

    public void setDiseaseRecommendation(String diseaseRecommendation) {
        this.diseaseRecommendation = diseaseRecommendation;
    }

    public String getDiseaseOtherInfo() {
        return diseaseOtherInfo;
    }

    public void setDiseaseOtherInfo(String diseaseOtherInfo) {
        this.diseaseOtherInfo = diseaseOtherInfo;
    }
}
