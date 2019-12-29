package denokela.com.medico.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import denokela.com.medico.entities.DQEntity;
import denokela.com.medico.entities.DiseaseSymptomsEntity;
import denokela.com.medico.Entries;
import denokela.com.medico.R;
import denokela.com.medico.viewmodels.DQViewModel;
import denokela.com.medico.viewmodels.DiseaseViewModel;

public class Diagnosis_questions extends AppCompatActivity{
    TextView tvdiagnosistitle;
    DQViewModel dqViewModel;
    DiseaseViewModel diseaseViewModel;

    SharedPreferences sharedPreferences;

    String disease="";
    List<DQEntity> diagnosisquestions;
    List<DiseaseSymptomsEntity> gastroSymptoms,choleraSymptoms,typhoidSymptoms,meningitisSymptoms,renalfailureSymptoms;

    ArrayList<Entries> entries = new ArrayList<Entries>();

    ArrayList<String> selectedItems = new ArrayList<>();
    ListView symptomslist;

    List<String> diseaseParameter;
    List<DQEntity> specificquestions;
    AlertDialog.Builder builder;

    int totalGastroCount,totalTyphoidCount,totalCholeraCount,totalRenalFailureCount,totalMeningitisCount,GastroCount, TyphoidCount,CholeraCount,RenalFailureCount,MeningitisCount;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_questions);
        tvdiagnosistitle = findViewById(R.id.tv_diagnosis_title);
        symptomslist = findViewById(R.id.listview_symptoms);
        symptomslist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        String[] diseases = {"Diarrhea", "Headache", "Abdominal Cramps", "Decreased Appetite", "Nausea", "Vomiting",
        "Dry Mouth", "Dehydration", "Fever", "Stomach Pain", "Fatigue", "Headache", "Extreme Cold", "Stiff Neck",
        "Swelling Limbs", "Chest Pain"};

        Collections.shuffle(Arrays.asList(diseases));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.rowlayout,R.id.ctv_symtoms,diseases);
        symptomslist.setAdapter(adapter);
        symptomslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = ((TextView)view).getText().toString();
                if(selectedItems.contains(selectedItem)){
                    selectedItems.remove(selectedItem); //Uncheck item
                }else{
                    selectedItems.add(selectedItem);
                }
            }
        });


        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);

        dqViewModel = ViewModelProviders.of(this).get(DQViewModel.class);
        diseaseViewModel = ViewModelProviders.of(this).get(DiseaseViewModel.class);

        dqViewModel.getAllDiagnosisQuestions().observe(Diagnosis_questions.this, new Observer<List<DQEntity>>() {
                    @Override
                    public void onChanged(List<DQEntity> dqEntities) {
                        diagnosisquestions = new ArrayList<>(dqEntities);
                        //Collections.shuffle(diagnosisquestions);
                    }
                });

        diseaseViewModel.getGastroSymptoms().observe(Diagnosis_questions.this, new Observer<List<DiseaseSymptomsEntity>>() {
            @Override
            public void onChanged(List<DiseaseSymptomsEntity> diseaseSymptomsEntities) {
                gastroSymptoms = new ArrayList<>(diseaseSymptomsEntities);
            }
        });

        diseaseViewModel.getCholeraSymptoms().observe(Diagnosis_questions.this, new Observer<List<DiseaseSymptomsEntity>>() {
            @Override
            public void onChanged(List<DiseaseSymptomsEntity> diseaseSymptomsEntities) {
                choleraSymptoms = new ArrayList<>(diseaseSymptomsEntities);
            }
        });

        diseaseViewModel.getTyphoidSymptoms().observe(Diagnosis_questions.this, new Observer<List<DiseaseSymptomsEntity>>() {
            @Override
            public void onChanged(List<DiseaseSymptomsEntity> diseaseSymptomsEntities) {
                typhoidSymptoms = new ArrayList<>(diseaseSymptomsEntities);
            }
        });

        diseaseViewModel.getMeningitisSymptoms().observe(Diagnosis_questions.this, new Observer<List<DiseaseSymptomsEntity>>() {
            @Override
            public void onChanged(List<DiseaseSymptomsEntity> diseaseSymptomsEntities) {
                meningitisSymptoms = new ArrayList<>(diseaseSymptomsEntities);
            }
        });

        diseaseViewModel.getRenalFailureSymptoms().observe(Diagnosis_questions.this, new Observer<List<DiseaseSymptomsEntity>>() {
            @Override
            public void onChanged(List<DiseaseSymptomsEntity> diseaseSymptomsEntities) {
                renalfailureSymptoms = new ArrayList<>(diseaseSymptomsEntities);
            }
        });

}

    public void showItems(View view) {
        specificquestions = new ArrayList<>();
        diseaseParameter = new ArrayList<>();

        GastroCount=0;
        RenalFailureCount = 0;
        TyphoidCount = 0;
        CholeraCount = 0;
        MeningitisCount = 0;

        totalGastroCount = 0;
        totalRenalFailureCount = 0;
        totalTyphoidCount = 0;
        totalCholeraCount = 0;
        totalMeningitisCount = 0;

        builder = new AlertDialog.Builder(this);

        if(selectedItems.size() >1){
            totalGastroCount = gastroSymptoms.size()+2;
            totalCholeraCount = choleraSymptoms.size()+2;
            totalTyphoidCount = typhoidSymptoms.size()+2;
            totalMeningitisCount = meningitisSymptoms.size()+2;
            totalRenalFailureCount = renalfailureSymptoms.size()+2;

            for(int i =0;i<selectedItems.size();i++) {
                for (int x = 0; x < gastroSymptoms.size(); x++) {
                    if (selectedItems.get(i).trim().equals(gastroSymptoms.get(x).getGastroenteritis().trim())) {
                        GastroCount++;
                    }
                }
                for (int x = 0; x < choleraSymptoms.size(); x++) {
                    if (selectedItems.get(i).trim().equals(choleraSymptoms.get(x).getCholera().trim())) {
                        CholeraCount++;
                    }
                }
                for (int x = 0; x < typhoidSymptoms.size(); x++) {
                    if (selectedItems.get(i).trim().equals(typhoidSymptoms.get(x).getTyphoid().trim())) {
                        TyphoidCount++;
                    }
                }
                for (int x = 0; x < meningitisSymptoms.size(); x++) {
                    if (selectedItems.get(i).trim().equals(meningitisSymptoms.get(x).getMeningitis().trim())) {
                        MeningitisCount++;
                    }
                }
                for (int x = 0; x < renalfailureSymptoms.size(); x++) {
                    if (selectedItems.get(i).trim().equals(renalfailureSymptoms.get(x).getRenalFailure().trim())) {
                        RenalFailureCount++;
                    }
                }
            }

                if(selectedItems.size() ==2 || selectedItems.size() ==3 || selectedItems.size() ==4 || selectedItems.size() ==5){
                    if(GastroCount>=1){
                        diseaseParameter.add("Gastroenteritis");
                    }
                    if(CholeraCount>=1){
                        diseaseParameter.add("Cholera");
                    }
                    if(TyphoidCount>=1){
                        diseaseParameter.add("Typhoid");
                    }
                    if(MeningitisCount>=1){
                        diseaseParameter.add("Meningitis");
                    }
                    if(RenalFailureCount>=1){
                        diseaseParameter.add("Renal Failure");
                    }
                }else if(selectedItems.size() ==6 || selectedItems.size() ==7 || selectedItems.size() ==8 || selectedItems.size() ==9 || selectedItems.size() ==10){
                    if(GastroCount>=2){
                        diseaseParameter.add("Gastroenteritis");
                    }
                    if(CholeraCount>=2){
                        diseaseParameter.add("Cholera");
                    }
                    if(TyphoidCount>=2){
                        diseaseParameter.add("Typhoid");
                    }
                    if(MeningitisCount>=2){
                        diseaseParameter.add("Meningitis");
                    }
                    if(RenalFailureCount>=2){
                        diseaseParameter.add("Renal Failure");
                    }
                }else if(selectedItems.size() ==11 || selectedItems.size() ==12 || selectedItems.size() ==13 || selectedItems.size() ==14 || selectedItems.size() ==15){
                    if(GastroCount>=3){
                        diseaseParameter.add("Gastroenteritis");
                    }
                    if(CholeraCount>=3){
                        diseaseParameter.add("Cholera");
                    }
                    if(TyphoidCount>=3){
                        diseaseParameter.add("Typhoid");
                    }
                    if(MeningitisCount>=3){
                        diseaseParameter.add("Meningitis");
                    }
                    if(RenalFailureCount>=3){
                        diseaseParameter.add("Renal Failure");
                    }
                } else if(selectedItems.size() == 16){
                    if(GastroCount>=4){
                        diseaseParameter.add("Gastroenteritis");
                    }
                    if(CholeraCount>=4){
                        diseaseParameter.add("Cholera");
                    }
                    if(TyphoidCount>=4){
                        diseaseParameter.add("Typhoid");
                    }
                    if(MeningitisCount>=4){
                        diseaseParameter.add("Meningitis");
                    }
                    if(RenalFailureCount>=4){
                        diseaseParameter.add("Renal Failure");
                    }
                }

            getdiseasequestion(diseaseParameter);
            showDialogQuestions(specificquestions);
        }
        else{
            Toast.makeText(this, "Kindly select a minimum of two symptoms", Toast.LENGTH_SHORT).show();
        }

    }


    private void showDialogQuestions(List<DQEntity> specificquestions) {
        int i=0;

        do{
            builder.setMessage(specificquestions.get(i).getDQ());

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    count++;
                    if(count == specificquestions.size()){
                        count =0;
                        transferTotalResult();
                    }
                }
            });

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if(specificquestions.get(count).getDisease().equals("Typhoid")){
                        TyphoidCount++;
                    }else if(specificquestions.get(count).getDisease().equals("Cholera")){
                        CholeraCount++;
                    }else if(specificquestions.get(count).getDisease().equals("Gastroenteritis")){
                        GastroCount++;
                    }else if(specificquestions.get(count).getDisease().equals("Renal Failure")){
                        RenalFailureCount++;
                    }else if(specificquestions.get(count).getDisease().equals("Meningitis")){
                        MeningitisCount++;
                    }
                    count++;
                    if(count == specificquestions.size()){
                        count =0;
                        transferTotalResult();
                    }

                }
            });

            builder.setNeutralButton("Stop", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    transferTotalResult();
                }
            });


            builder.setCancelable(false);
            builder.show();
            i++;

        }while(i<specificquestions.size());

    }

    private void getdiseasequestion(List<String> diseaseParameter) {

        for(int i =0; i<diseaseParameter.size();i++){
            String value = diseaseParameter.get(i);
            for(int x=0;x<diagnosisquestions.size();x++){
                if(value.trim().equals(diagnosisquestions.get(x).getDisease())){
                    specificquestions.add(new DQEntity(diagnosisquestions.get(x).getDQ(),diagnosisquestions.get(x).getDisease()));
                }
            }
        }

        Collections.shuffle(specificquestions);

    }

    public void transferTotalResult(){
            //Casting the Values as double to perform arithmetic operation and returning them back to int
            GastroCount= (int) (((double)GastroCount / (double) totalGastroCount) * 100);
            CholeraCount= (int) (((double)CholeraCount / (double) totalCholeraCount) * 100);
            TyphoidCount = (int) (((double)TyphoidCount / (double) totalTyphoidCount) * 100);
            MeningitisCount = (int) (((double)MeningitisCount / (double) totalMeningitisCount) * 100);
            RenalFailureCount = (int) (((double)RenalFailureCount / (double) totalRenalFailureCount) * 100);

              if(GastroCount>10){
                  entries.add(new Entries("Gastroenteritis",GastroCount));
              }
              if(CholeraCount>10){
                  entries.add(new Entries("Cholera", CholeraCount));
              }
              if(TyphoidCount>10){
                  entries.add(new Entries("Typhoid", TyphoidCount));
              }
              if(MeningitisCount>10){
                  entries.add(new Entries("Meningitis",MeningitisCount));
              }
              if(RenalFailureCount>10){
                  entries.add(new Entries("Renal Failure", RenalFailureCount));
              }

            startIntent();

    }

    private void startIntent(){
        Collections.sort(entries, new Comparator<Entries>() {
            @Override
            public int compare(Entries entries, Entries t1) {
                return t1.getPercent().compareTo(entries.getPercent());
            }
        });

        Integer patientID = sharedPreferences.getInt("CurrentID",1);


        //Above sorting mechanism is for API level 24 or higher
        //entries.sort(Comparator.comparing(Entries::getPercent));
        Intent intent = new Intent(Diagnosis_questions.this,Diagnosis_results.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Disease",entries);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}



