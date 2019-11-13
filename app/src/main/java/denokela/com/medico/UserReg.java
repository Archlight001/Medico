package denokela.com.medico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class UserReg extends AppCompatActivity implements View.OnClickListener {

    EditText fName,lName,age;
    Button regBtn;
    String firstName,lastName,ageVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);

        fName = findViewById(R.id.etFirstname);
        lName = findViewById(R.id.etLastname);
        age = findViewById(R.id.etAge);
        regBtn= findViewById(R.id.btnReg);

        regBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        firstName = fName.getText().toString();
        lastName = lName.getText().toString();
        ageVal = age.getText().toString();

        ExecDatabase execDatabase = new ExecDatabase(new ExecDatabase.AsyncRespone() {
            @Override
            public void processfinish(List output) {
                if(output.size()>0){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Data not registered",Toast.LENGTH_LONG);
                }
            }
        },"insertData",getApplicationContext());


        execDatabase.execute(firstName,lastName,ageVal);
    }
}
