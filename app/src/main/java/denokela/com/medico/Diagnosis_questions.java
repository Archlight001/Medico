package denokela.com.medico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Diagnosis_questions extends AppCompatActivity{
    TextView tvquestions;
    Button nextButton;
    RadioGroup radioGroup;
    RadioButton radioButtonYes,radioButtonNo,radBtn;
    DQViewModel dqViewModel;

    int questioncounttotal=0;
    int questioncount=0;

    int countGastro;
    int countCholera;
    int countMeningitis;
    int countTyphoid;
    int countRenalFailure;

    int totalGastroCount = 6;
    int totalCholeraCount = 6;
    int totalMeningitisCount = 5;
    int totalTyphoidCount = 6;
    int totalRenalFailureCount = 4;

    int radioButtonValue;

    SharedPreferences sharedPreferences;

    String disease="";
    List<DQSimilarEntity> similarquestions;
    List<DQEntity> diagnosisquestions;
    ArrayList<Entries> entries = new ArrayList<Entries>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_questions);
        tvquestions = findViewById(R.id.tv_questionns);
        nextButton = findViewById(R.id.nextBtn);
        radioGroup = findViewById(R.id.rg_answers);
        radioButtonYes = findViewById(R.id.rb_yes);
        radioButtonNo = findViewById(R.id.rb_no);

        sharedPreferences = getSharedPreferences("userNo",MODE_PRIVATE);

        dqViewModel = ViewModelProviders.of(this).get(DQViewModel.class);
        dqViewModel.getAllSimilarQuestions().observe(this, new Observer<List<DQSimilarEntity>>() {
            @Override
            public void onChanged(List<DQSimilarEntity> dqSimilarEntities) {
                similarquestions = new ArrayList<>(dqSimilarEntities);
                questioncounttotal = similarquestions.size();

                dqViewModel.getAllDiagnosisQuestions().observe(Diagnosis_questions.this, new Observer<List<DQEntity>>() {
                    @Override
                    public void onChanged(List<DQEntity> dqEntities) {
                        diagnosisquestions = new ArrayList<>(dqEntities);
                        Collections.shuffle(diagnosisquestions);
                        questioncounttotal+= diagnosisquestions.size();
                        showNextQuestion();


                    }
                });


            }
        });









        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioButtonYes.isChecked() || radioButtonNo.isChecked()){
                    radioButtonValue = radioGroup.getCheckedRadioButtonId();
                    radBtn = findViewById(radioButtonValue);
                    incrementCountDiseases();
                    showNextQuestion();
                }else{
                    Toast.makeText(Diagnosis_questions.this, "Kindly Select an Option", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private void showNextQuestion() {
        radioGroup.clearCheck();
        //Toast.makeText(this, String.valueOf(questioncounttotal), Toast.LENGTH_SHORT).show();

        if(questioncount<questioncounttotal && questioncount < 5){
           tvquestions.setText(similarquestions.get(questioncount).getSDQ());
           questioncount++;
        }else if(questioncount<questioncounttotal && questioncount >= 5){
            tvquestions.setText(diagnosisquestions.get(questioncount-5).getDQ());
            disease= diagnosisquestions.get(questioncount-5).getDisease();
            questioncount++;

             if(questioncounttotal>10 && countGastro==totalGastroCount){
                countGastro= (int) ((((double)countGastro-0.6) / (double) totalGastroCount) * 100);
                entries.add(new Entries("Gastroenteritis",countGastro));
                if(countTyphoid>=3){
                    countTyphoid = (int) (((double)countTyphoid / (double) totalTyphoidCount) * 100);
                    entries.add(new Entries("Typhoid", countTyphoid));
                }else if(countCholera>=3){
                    countCholera= (int) (((double)countCholera / (double) totalCholeraCount) * 100);
                    entries.add(new Entries("Cholera", countCholera));
                }else if(countMeningitis>=3){
                    countMeningitis = (int) (((double)countMeningitis / (double) totalMeningitisCount) * 100);
                    entries.add(new Entries("Meningitis",countMeningitis));
                }else if(countRenalFailure>=3){
                    countRenalFailure = (int) (((double)countRenalFailure / (double) totalRenalFailureCount) * 100);
                    entries.add(new Entries("Renal Failure", countRenalFailure));
                }
                startIntent();
            }
            else if(questioncounttotal>10 && countCholera==totalCholeraCount){
                countCholera= (int) ((((double)countCholera-0.6) / (double) totalCholeraCount) * 100);
                entries.add(new Entries("Cholera", countCholera));
                if(countTyphoid>=3){
                    countTyphoid = (int) (((double)countTyphoid / (double) totalTyphoidCount) * 100);
                    entries.add(new Entries("Typhoid", countTyphoid));
                }else if(countGastro>=3){
                    countGastro= (int) (((double)countGastro / (double) totalGastroCount) * 100);
                    entries.add(new Entries("Gastroenteritis", countGastro));
                }else if(countMeningitis>=3){
                    countMeningitis = (int) (((double)countMeningitis / (double) totalMeningitisCount) * 100);
                    entries.add(new Entries("Meningitis",countMeningitis));
                }else if(countRenalFailure>=3){
                    countRenalFailure = (int) (((double)countRenalFailure / (double) totalRenalFailureCount) * 100);
                    entries.add(new Entries("Renal Failure", countRenalFailure));
                }
                startIntent();
            }
            else if(questioncounttotal>10 && countTyphoid==totalTyphoidCount){
                countTyphoid = (int) ((((double)countTyphoid-0.6) / (double) totalTyphoidCount) * 100);
                entries.add(new Entries("Typhoid", countTyphoid));
                if(countCholera>=3){
                    countCholera= (int) (((double)countCholera / (double) totalCholeraCount) * 100);
                    entries.add(new Entries("Cholera", countCholera));
                }else if(countGastro>=3){
                    countGastro= (int) (((double)countGastro / (double) totalGastroCount) * 100);
                    entries.add(new Entries("Gastroenteritis", countGastro));
                }else if(countMeningitis>=3){
                    countMeningitis = (int) (((double)countMeningitis / (double) totalMeningitisCount) * 100);
                    entries.add(new Entries("Meningitis",countMeningitis));
                }else if(countRenalFailure>=3){
                    countRenalFailure = (int) (((double)countRenalFailure / (double) totalRenalFailureCount) * 100);
                    entries.add(new Entries("Renal Failure", countRenalFailure));
                }
                startIntent();
            }
            else if(questioncounttotal>10 && countMeningitis==totalMeningitisCount){
                countMeningitis = (int) ((((double)countMeningitis-0.5) / (double) totalMeningitisCount) * 100);
                entries.add(new Entries("Meningitis",countMeningitis));

                if(countCholera>=3){
                    countCholera= (int) (((double)countCholera / (double) totalCholeraCount) * 100);
                    entries.add(new Entries("Cholera", countCholera));
                }else if(countGastro>=3){
                    countGastro= (int) (((double)countGastro / (double) totalGastroCount) * 100);
                    entries.add(new Entries("Gastroenteritis", countGastro));
                }else if(countTyphoid>=3){
                    countTyphoid = (int) (((double)countTyphoid / (double) totalTyphoidCount) * 100);
                    entries.add(new Entries("Typhoid", countTyphoid));
                }else if(countRenalFailure>=3){
                    countRenalFailure = (int) (((double)countRenalFailure / (double) totalRenalFailureCount) * 100);
                    entries.add(new Entries("Renal Failure", countRenalFailure));
                }
                startIntent();
            }
            else if(questioncounttotal>10 && countRenalFailure==totalRenalFailureCount){
                countRenalFailure = (int) ((((double)countRenalFailure-0.4) / (double) totalRenalFailureCount) * 100);
                entries.add(new Entries("Renal Failure", countRenalFailure));

                if(countCholera>=3){
                    countCholera= (int) (((double)countCholera / (double) totalCholeraCount) * 100);
                    entries.add(new Entries("Cholera", countCholera));
                }else if(countGastro>=3){
                    countGastro= (int) (((double)countGastro / (double) totalGastroCount) * 100);
                    entries.add(new Entries("Gastroenteritis", countGastro));
                }else if(countTyphoid>=3){
                    countTyphoid = (int) (((double)countTyphoid / (double) totalTyphoidCount) * 100);
                    entries.add(new Entries("Typhoid", countTyphoid));
                }else if(countMeningitis>=3){
                    countMeningitis = (int) (((double)countMeningitis / (double) totalMeningitisCount) * 100);
                    entries.add(new Entries("Meningitis",countMeningitis));
                }
                startIntent();
            }


        }


        else{
            //Casting the Values as double to perform arithmetic operation and returning them back to int
            countGastro= (int) (((double)countGastro / (double) totalGastroCount) * 100);
            countCholera= (int) (((double)countCholera / (double) totalCholeraCount) * 100);
            countTyphoid = (int) (((double)countTyphoid / (double) totalTyphoidCount) * 100);
            countMeningitis = (int) (((double)countMeningitis / (double) totalMeningitisCount) * 100);
            countRenalFailure = (int) (((double)countRenalFailure / (double) totalRenalFailureCount) * 100);

              if(countGastro>30){
                  entries.add(new Entries("Gastroenteritis",countGastro));
              }else if(countCholera>30){
                  entries.add(new Entries("Cholera", countCholera));
              }else if(countTyphoid>30){
                  entries.add(new Entries("Typhoid", countTyphoid));
              } else if(countMeningitis>30){
                  entries.add(new Entries("Meningitis",countMeningitis));
              } else if(countRenalFailure>30){
                  entries.add(new Entries("Renal Failure", countRenalFailure));
              }

            startIntent();
        }

    }

    private void startIntent() {
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


    private void incrementCountDiseases() {
        if(questioncount==1 && radBtn.getText().equals("Yes")){
            countCholera++;
            countTyphoid++;
            countMeningitis++;
        }else if(questioncount==2 && radBtn.getText().equals("Yes")){
            countCholera++;
            countGastro++;
            countMeningitis++;
        }else if(questioncount==3 && radBtn.getText().equals("Yes")){
            countGastro++;
            countTyphoid++;
        }else if(questioncount==4 && radBtn.getText().equals("Yes")){
            countCholera++;
            countGastro++;
        }else if(questioncount==5 && radBtn.getText().equals("Yes")){
            countCholera++;
            countGastro++;
        }else if(radBtn.getText().equals("Yes") && disease.equals("Gastroenteritis")){
            countGastro++;
        }else if(radBtn.getText().equals("Yes") && disease.equals("Cholera")){
            countCholera++;
        }else if(radBtn.getText().equals("Yes") && disease.equals("Typhoid")){
            countTyphoid++;
        }else if(radBtn.getText().equals("Yes") && disease.equals("Meningitis")){
            countMeningitis++;
        }else if(radBtn.getText().equals("Yes") && disease.equals("Renal Failure")){
            countRenalFailure++;
        }
    }


}
