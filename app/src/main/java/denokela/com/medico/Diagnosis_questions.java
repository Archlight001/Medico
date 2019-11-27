package denokela.com.medico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Diagnosis_questions extends AppCompatActivity {
    TextView tvquestions;
    Button nextButton;
    RadioGroup radioGroup;
    RadioButton radioButtonYes,radioButtonNo;
    DQViewModel dqViewModel;

    int questioncounttotal;
    int questioncount;

    int countGastro;
    int countCholera;
    int countMeningitis;
    int countTyphoid;
    int countRenalFailure;

    String disease="";
    List<DQSimilarEntity> similarquestions;
    List<DQEntity> diagnosisquestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_questions);
        tvquestions = findViewById(R.id.tv_questionns);
        nextButton = findViewById(R.id.nextBtn);
        radioGroup = findViewById(R.id.rg_answers);
        radioButtonYes = findViewById(R.id.rb_yes);
        radioButtonNo = findViewById(R.id.rb_no);


        dqViewModel = ViewModelProviders.of(this).get(DQViewModel.class);
        dqViewModel.getAllSimilarQuestions().observe(this, new Observer<List<DQSimilarEntity>>() {
            @Override
            public void onChanged(List<DQSimilarEntity> dqSimilarEntities) {
                similarquestions = new ArrayList<>(dqSimilarEntities);
                questioncounttotal = similarquestions.size();



            }
        });

        dqViewModel.getAllDiagnosisQuestions().observe(this, new Observer<List<DQEntity>>() {
            @Override
            public void onChanged(List<DQEntity> dqEntities) {
                diagnosisquestions = new ArrayList<>(dqEntities);
                Collections.shuffle(diagnosisquestions);
                questioncounttotal+= diagnosisquestions.size();

                showNextQuestion();
            }
        });



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioButtonYes.isChecked() || radioButtonNo.isChecked()){
                    int radioButtonValue = radioGroup.getCheckedRadioButtonId();
                    RadioButton radBtn = findViewById(radioButtonValue);
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
                    }
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

        }else{
            Toast.makeText(this, String.valueOf(countGastro), Toast.LENGTH_SHORT).show();
            finish();
        }

        //Toast.makeText(this, String.valueOf(questioncount), Toast.LENGTH_SHORT).show();
    }


}
