package denokela.com.medico;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class UserReg extends AppCompatActivity implements View.OnClickListener {

    EditText fName,lName,age;
    Button regBtn;
    String firstName,lastName;
    int ageVal;

    public static final String EXTRA_FNAME ="za-fname";
    public static final String EXTRA_LNAME ="za-lname";
    public static final String EXTRA_AGE ="za-age";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);

        fName = findViewById(R.id.etFirstname);
        lName = findViewById(R.id.etLastname);
        age = findViewById(R.id.etAge);
        regBtn= findViewById(R.id.btnReg);

        regBtn.setOnClickListener(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add User");

    }

    @Override
    public void onClick(View view) {
        firstName = fName.getText().toString();
        lastName = lName.getText().toString();
        String agestring = age.getText().toString();

        if(firstName.trim().isEmpty() || lastName.trim().isEmpty() || agestring.trim().isEmpty()){
            Toast.makeText(this, "Please Fill in the blank fields", Toast.LENGTH_SHORT).show();
            return;
        }
        ageVal = Integer.parseInt(age.getText().toString());

        Intent data = new Intent();
        data.putExtra(EXTRA_FNAME,firstName);
        data.putExtra(EXTRA_LNAME,lastName);
        data.putExtra(EXTRA_AGE,ageVal);

        setResult(RESULT_OK,data);
        finish();


    }
}
