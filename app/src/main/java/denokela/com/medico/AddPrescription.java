package denokela.com.medico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.List;

public class AddPrescription extends AppCompatActivity {
    private EditText etDrugName;
    private Spinner spinDrugform, spinPatName;
    private NumberPicker npDrugamount, npDruginterval,npTotaldays;

    UserViewModel userViewModel;

    public static final String EXTRA_PATIENT_NAME="denokela.com.medico.EXTRA_PATIENT_NAME";
    public static final String EXTRA_DRUG_NAME="denokela.com.medico.EXTRA_DRUG_NAME";
    public static final String EXTRA_DRUG_FORM="denokela.com.medico.EXTRA_DRUG_FORM";
    public static final String EXTRA_DRUG_AMOUNT="denokela.com.medico.EXTRA_DRUG_AMOUNT";
    public static final String EXTRA_DRUG_INTERVAL="denokela.com.medico.EXTRA_DRUG_INTERVAL";
    public static final String EXTRA_TOTAL_DAYS="denokela.com.medico.EXTRA_TOTAL_DAYS";
    public static final String EXTRA_COUNT="denokela.com.medico.EXTRA_COUNT";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);

        etDrugName = findViewById(R.id.etDrugName);
        spinDrugform = findViewById(R.id.spinner_drugform);
        spinPatName = findViewById(R.id.spinner_patname);
        npDrugamount = findViewById(R.id.num_picker_drugamount);
        npDruginterval = findViewById(R.id.num_picker_druginterval);
        npTotaldays = findViewById(R.id.num_picker_totaldays);

        npDrugamount.setMinValue(1);
        npDrugamount.setMaxValue(10);

        npDruginterval.setMinValue(1);
        npDruginterval.setMaxValue(24);

        npTotaldays.setMinValue(1);
        npTotaldays.setMaxValue(100);

        String[] forms = {"Tablet", "Syrup", "Injection"};
        ArrayAdapter<String> formadapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, forms);
        spinDrugform.setAdapter(formadapter);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                String[] names = new String[userEntities.size()];
                for (int i = 0; i < names.length; i++) {
                    names[i] = userEntities.get(i).getFirstName() + " " + userEntities.get(i).getLastName();
                }
                ArrayAdapter<String> nameadapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, names);
                spinPatName.setAdapter(nameadapter);
            }
        });



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Prescription");

    }

    private void savePrescription(){
        String drugname = etDrugName.getText().toString();
        String patientname = spinPatName.getSelectedItem().toString();
        String drugform = spinDrugform.getSelectedItem().toString();
        int drugamount = npDrugamount.getValue();
        int druginterval = npDruginterval.getValue();
        int totaldays = npTotaldays.getValue();
        int count = totaldays*drugamount;

        Intent data = new Intent();
        data.putExtra(EXTRA_PATIENT_NAME,patientname);
        data.putExtra(EXTRA_DRUG_NAME,drugname);
        data.putExtra(EXTRA_DRUG_FORM,drugform);
        data.putExtra(EXTRA_DRUG_INTERVAL,druginterval);
        data.putExtra(EXTRA_DRUG_AMOUNT,drugamount);
        data.putExtra(EXTRA_TOTAL_DAYS,totaldays);
        data.putExtra(EXTRA_COUNT,count);

        setResult(RESULT_OK,data);
        finish();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_prescription_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_prescription:
                savePrescription();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
