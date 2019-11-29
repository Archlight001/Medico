package denokela.com.medico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    }
}