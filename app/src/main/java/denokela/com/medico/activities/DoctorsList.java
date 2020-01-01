package denokela.com.medico.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import denokela.com.medico.R;
import denokela.com.medico.adapter.DoctorsAdapter;
import denokela.com.medico.entities.DoctorsEntity;
import denokela.com.medico.viewmodels.DoctorsViewModel;

public class DoctorsList extends AppCompatActivity {

    DoctorsViewModel doctorsViewModel;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);

        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences.getInt("DoctorsList",0)==0){
            AlertDialog.Builder firstbuild = new AlertDialog.Builder(this);
            firstbuild.setMessage("Note that all information provided are for trial purposes and are not real");
            firstbuild.show();
            editor.putInt("DoctorsList",1);
            editor.apply();
        }
        TextView tvTitle = findViewById(R.id.tv_showingDoctors);
        String diseaseName = getIntent().getStringExtra("DiseaseName");
        tvTitle.setText("Showing Doctors that Specializes in "+ diseaseName);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_doctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DoctorsAdapter adapter = new DoctorsAdapter();
        recyclerView.setAdapter(adapter);

        doctorsViewModel = ViewModelProviders.of(this).get(DoctorsViewModel.class);
//        doctorsViewModel.getAllDoctors.observe(this, new Observer<List<DoctorsEntity>>() {
//            @Override
//            public void onChanged(List<DoctorsEntity> doctorsEntities) {
//                adapter.setDoctors(doctorsEntities,getApplicationContext());
//            }
//        });

        doctorsViewModel.getSpecificDoctors("%"+diseaseName+"%").observe(this, new Observer<List<DoctorsEntity>>() {
            @Override
            public void onChanged(List<DoctorsEntity> doctorsEntities) {
                adapter.setDoctors(doctorsEntities,getApplicationContext());
            }
        });

        adapter.setOnItemClickListener(new DoctorsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String address) {
                Intent intent = new Intent(DoctorsList.this,DoctorLocationMap.class);
                intent.putExtra("Address",address);
                startActivity(intent);
            }
        });


    }
}
