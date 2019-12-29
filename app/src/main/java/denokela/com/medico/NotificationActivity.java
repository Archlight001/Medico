package denokela.com.medico;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;


import denokela.com.medico.activities.MainActivity;
import denokela.com.medico.entities.PrescriptionEntity;
import denokela.com.medico.viewmodels.PrescriptionViewModel;

public class NotificationActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Integer prescriptionId = Integer.parseInt(getIntent().getStringExtra("key"));
        String name = getIntent().getStringExtra("name");
        PrescriptionViewModel prescriptionViewModel = ViewModelProviders.of(this).get(PrescriptionViewModel.class);
        prescriptionViewModel.getCertainPrescription(prescriptionId).observe(this, new Observer<List<PrescriptionEntity>>() {
            @Override
            public void onChanged(List<PrescriptionEntity> prescriptionEntities) {
                Integer count = prescriptionEntities.get(0).getCount() -1;
                Toast.makeText(NotificationActivity.this, String.valueOf(prescriptionEntities.size()), Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                builder.setMessage("\n" + "Take " + prescriptionEntities.get(0).getDoseNumber() +" " + prescriptionEntities.get(0).getDrugForm() + " of " + prescriptionEntities.get(0).getDrugName());
                TextView title = new TextView(NotificationActivity.this);
                title.setText(name);
                title.setBackgroundColor(Color.DKGRAY);
                title.setPadding(20, 20, 20, 20);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);
                builder.setCustomTitle(title)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        prescriptionViewModel.updateCount(count,prescriptionId);
                                          finish();

                                    }
                                });
                builder.show();
                builder.setCancelable(true);
            }
        });
    }

}

