package denokela.com.medico.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import denokela.com.medico.Entries;
import denokela.com.medico.R;
import denokela.com.medico.adapter.DiseaseAdapter;

public class Diagnosis_results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_results);
        //Intent intent = getIntent();

        Bundle bundleObject = getIntent().getExtras();
        ArrayList<Entries> values = (ArrayList<Entries>) bundleObject.getSerializable("Disease");

        //ArrayList<String> disease = intent.getStringArrayListExtra("Disease");
        //ArrayList<String> percent = intent.getStringArrayListExtra("Percent");
        //Toast.makeText(this, entries.get(0).getName(), Toast.LENGTH_SHORT).show();
        RecyclerView recyclerView = findViewById(R.id.disease_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //Toast.makeText(this, String.valueOf(values.get(1).getName()), Toast.LENGTH_SHORT).show();
        DiseaseAdapter diseaseAdapter = new DiseaseAdapter();
        diseaseAdapter.setDiseases(values);
        recyclerView.setAdapter(diseaseAdapter);

        diseaseAdapter.setOnItemClickListener(new DiseaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Entries entries) {
                Intent intent = new Intent(Diagnosis_results.this, DiseaseInformation.class);
                intent.putExtra("Name", entries.getName());
                startActivity(intent);
                //Toast.makeText(Diagnosis_results.this, entries.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
