package denokela.com.medico.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import denokela.com.medico.AppDatabase;
import denokela.com.medico.dao.PrescriptionDao;
import denokela.com.medico.entities.PrescriptionEntity;
import denokela.com.medico.R;
import denokela.com.medico.fragments.TimePickerFragment;
import denokela.com.medico.entities.UserEntity;
import denokela.com.medico.viewmodels.PrescriptionViewModel;
import denokela.com.medico.viewmodels.UserViewModel;

public class AddPrescription extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private EditText etDrugName;
    private Spinner spinDrugform, spinPatName;
    private NumberPicker npDrugamount, npDruginterval, npTotaldays;
    Button btnSetTIme;
    TextView tvShowtime;

    Calendar c = Calendar.getInstance();

    UserViewModel userViewModel;


    Integer hour = 0;
    Integer minute = 0;


    public static final String EXTRA_PATIENT_ID = "denokela.com.medico.EXTRA_PATIENT_NAME";
    public static final String EXTRA_DRUG_NAME = "denokela.com.medico.EXTRA_DRUG_NAME";
    public static final String EXTRA_DRUG_FORM = "denokela.com.medico.EXTRA_DRUG_FORM";
    public static final String EXTRA_DRUG_AMOUNT = "denokela.com.medico.EXTRA_DRUG_AMOUNT";
    public static final String EXTRA_DRUG_INTERVAL = "denokela.com.medico.EXTRA_DRUG_INTERVAL";
    public static final String EXTRA_TOTAL_DAYS = "denokela.com.medico.EXTRA_TOTAL_DAYS";
    public static final String EXTRA_COUNT = "denokela.com.medico.EXTRA_COUNT";
    public static final String ALARM_HOUR = "denokela.com.medico.ALARM_HOUR";
    public static final String ALARM_MINUTE = "denokela.com.medico.ALARM_MINUTE";

    private static final String TAG = "AddPrescription";

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
        btnSetTIme = findViewById(R.id.btn_setInitialtime);
        tvShowtime = findViewById(R.id.tv_initialtime);

        btnSetTIme.setOnClickListener(this);

        List<Integer> patId = new ArrayList<>();

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
                    patId.add(userEntities.get(i).getUserid());
                }
                ArrayAdapter<String> nameadapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, names);
                spinPatName.setAdapter(nameadapter);
            }
        });


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Prescription");

    }

    private void savePrescription() {
        String drugname = etDrugName.getText().toString();
        int patientID = spinPatName.getSelectedItemPosition() + 1;
        String drugform = spinDrugform.getSelectedItem().toString();
        int drugamount = npDrugamount.getValue();
        int druginterval = npDruginterval.getValue();
        int totaldays = npTotaldays.getValue();
        int count = (totaldays * (24 / druginterval));

        String patientName = spinPatName.getSelectedItem().toString();

        if (drugname.trim().length() == 0) {
            Toast.makeText(this, "Kindly enter a drug name", Toast.LENGTH_SHORT).show();
        } else if (hour == 0 || minute == 0) {
            Toast.makeText(this, "Kindly enter a time for the first dose", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You're about to add a new Prescription for " + patientName);
            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Asynctask asynctask = new Asynctask(new Asynctask.AsyncResponse() {
                        @Override
                        public void processfinish(List<PrescriptionEntity> output) {
                            if (output.size() == 0) {
                                dialogInterface.dismiss();
                                Intent data = new Intent();
                                data.putExtra(EXTRA_PATIENT_ID, patientID);
                                data.putExtra(EXTRA_DRUG_NAME, drugname);
                                data.putExtra(EXTRA_DRUG_FORM, drugform);
                                data.putExtra(EXTRA_DRUG_INTERVAL, druginterval);
                                data.putExtra(EXTRA_DRUG_AMOUNT, drugamount);
                                data.putExtra(EXTRA_TOTAL_DAYS, totaldays);
                                data.putExtra(EXTRA_COUNT, count);
                                data.putExtra(ALARM_HOUR, hour);
                                data.putExtra(ALARM_MINUTE, minute);

                                Log.d(TAG, "onChanged: Adding preescription");
                                setResult(RESULT_OK, data);

                                Toast.makeText(AddPrescription.this, "Adding Prescription", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Prescription Already Exists for this Patient and Drug", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, getApplicationContext(), patientID, drugname);
                    asynctask.execute();

                }
            });
            builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();


        }

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

    @Override
    public void onClick(View view) {
        DialogFragment timepicker = new TimePickerFragment();
        timepicker.show(getSupportFragmentManager(), "timepicker");
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourofDay, int minute) {
        c.set(Calendar.HOUR_OF_DAY, hourofDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        hour = hourofDay;
        this.minute = minute;
        updateTimeText(c);

    }

    private void updateTimeText(Calendar c) {
        String timeText = "First Prescription to be taken at: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        tvShowtime.setText(timeText);
    }
}

class Asynctask extends AsyncTask<List<PrescriptionEntity>, Void, List<PrescriptionEntity>> {

    public interface AsyncResponse {
        void processfinish(List<PrescriptionEntity> output);
    }

    int patientID;
    String drugname;
    Context context;
    public AsyncResponse delegate = null;

    public Asynctask(AsyncResponse delegate, Context context, int patientID, String drugname) {
        this.delegate = delegate;
        this.patientID = patientID;
        this.drugname = drugname;
        this.context = context;
    }


    @Override
    protected List<PrescriptionEntity> doInBackground(List<PrescriptionEntity>... lists) {

        return AppDatabase.getInstance(context).prescriptionDao().getPrescriptionPatientDrug(patientID, drugname);
    }

    @Override
    protected void onPostExecute(List<PrescriptionEntity> prescriptionEntities) {
        delegate.processfinish(prescriptionEntities);
    }

}
