package denokela.com.medico.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import denokela.com.medico.entities.DiseaseEntity;
import denokela.com.medico.R;
import denokela.com.medico.viewmodels.DiseaseViewModel;

public class DiseaseInformation extends AppCompatActivity {
    private TextView tvDiseaseName, tvDiseaseDefinition, tvDiseaseSymptoms, tvDiseaseRecommendation, tvDiseaseOtherInfo;
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_information);

        tvDiseaseName = findViewById(R.id.tv_diseaseTitle);
        tvDiseaseDefinition = findViewById(R.id.tv_diseaseDefinition);
        tvDiseaseSymptoms = findViewById(R.id.tv_diseaseSymptoms);
        tvDiseaseRecommendation = findViewById(R.id.tv_diseaseRecommendation);
        tvDiseaseOtherInfo = findViewById(R.id.tv_diseaseOtherInfo);
        mapButton = findViewById(R.id.btn_sendtomap);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Name").toUpperCase();
        if(name.equalsIgnoreCase("Typhoid")){
            name = "TYPHOID FEVER";
        }

        DiseaseViewModel diseaseViewModel = ViewModelProviders.of(this).get(DiseaseViewModel.class);
        diseaseViewModel.getDiseases(name).observe(this, new Observer<List<DiseaseEntity>>() {
            @Override
            public void onChanged(List<DiseaseEntity> diseaseEntities) {
                tvDiseaseName.setText(diseaseEntities.get(0).getDiseaseName());
                tvDiseaseDefinition.setText(diseaseEntities.get(0).getDiseaseDefinition());
                tvDiseaseSymptoms.setText(diseaseEntities.get(0).getDiseaseSymptoms());
                tvDiseaseRecommendation.setText(diseaseEntities.get(0).getDiseaseRecommendation());
                tvDiseaseOtherInfo.setText(diseaseEntities.get(0).getDiseaseOtherInfo());
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DiseaseInformation.this, MainActivity.class);
                intent1.putExtra("fragment_to_load","Map");
                startActivity(intent1);
                finish();
            }
        });



    }
}
