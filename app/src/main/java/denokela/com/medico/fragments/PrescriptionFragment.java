package denokela.com.medico.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import denokela.com.medico.activities.AddPrescription;
import denokela.com.medico.entities.PrescriptionEntity;
import denokela.com.medico.R;
import denokela.com.medico.entities.UserEntity;
import denokela.com.medico.activities.UserReg;
import denokela.com.medico.adapter.PrescriptionAdapter;
import denokela.com.medico.viewmodels.PrescriptionViewModel;
import denokela.com.medico.viewmodels.UserViewModel;

public class PrescriptionFragment extends Fragment implements View.OnClickListener {
    TextView tvInstruction;
    PrescriptionViewModel prescriptionViewModel;
    UserViewModel userViewModel;
    RecyclerView recyclerView;
    List<String> users = new ArrayList<>();
    List<Integer> userid = new ArrayList<>();

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public Integer channellerID=0;


    FloatingActionButton btnaddPrescription;

    public static final int ADD_PRESCRIPTION_REQUEST = 2;
    public static final int ADD_USER_REQUEST = 3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_prescription, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPref = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        editor = sharedPref.edit();

        tvInstruction = view.findViewById(R.id.tv_instruction);
        btnaddPrescription = view.findViewById(R.id.btn_add_prescription);
        btnaddPrescription.setOnClickListener(this);


        recyclerView = view.findViewById(R.id.recycler_view_prescription);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final PrescriptionAdapter prescriptionAdapter = new PrescriptionAdapter();
        recyclerView.setAdapter(prescriptionAdapter);
        userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                for (int i = 0; i < userEntities.size(); i++) {
                    users.add(userEntities.get(i).getFirstName() + " " + userEntities.get(i).getLastName());
                    userid.add(userEntities.get(i).getUserid());
                }
                users.add("Add a New Patient");
            }
        });


        int value = sharedPref.getInt("CurrentID", 1);
        prescriptionViewModel = ViewModelProviders.of(getActivity()).get(PrescriptionViewModel.class);
        prescriptionViewModel.getCertainPrescription(value).observe(this, new Observer<List<PrescriptionEntity>>() {
            @Override
            public void onChanged(List<PrescriptionEntity> prescriptionEntities) {
                if (prescriptionEntities.size() == 0) {
                    tvInstruction.setVisibility(View.VISIBLE);
                    return;
                }
                Toast.makeText(getContext(), prescriptionEntities.get(0).getDrugName(), Toast.LENGTH_SHORT).show();
                prescriptionAdapter.setPrescriptions(prescriptionEntities);

            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), AddPrescription.class);
        startActivityForResult(intent, ADD_PRESCRIPTION_REQUEST);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.prescription_fragment_menu, menu);
        return;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_user) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select a User");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.select_dialog_singlechoice);
            for (int i = 0; i < users.size(); i++) {
                arrayAdapter.add(users.get(i));
            }
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (arrayAdapter.getItem(i).equals("Add a New Patient")) {
                        dialogInterface.dismiss();
                        Intent gotoUserList = new Intent(getContext(), UserReg.class);
                        gotoUserList.putExtra("PrescriptionFragment","pFragment");
                        startActivityForResult(gotoUserList,ADD_USER_REQUEST);
                        users.clear();
                        userid.clear();
                        return;
                    }
                    Integer preferenceId = userid.get(i);
                    editor.putInt("CurrentID", preferenceId);
                    editor.apply();

                    String strname = arrayAdapter.getItem(i);
                    AlertDialog.Builder builderinner = new AlertDialog.Builder(getContext());
                    builderinner.setTitle("Username Selected is: ");
                    builderinner.setMessage(strname);
                    builderinner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().recreate();
                            dialogInterface.dismiss();
                        }
                    });
                    builderinner.show();
                }
            });
            builder.show();

            return true;
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PRESCRIPTION_REQUEST && resultCode == getActivity().RESULT_OK) {
            String drugname = data.getStringExtra(AddPrescription.EXTRA_DRUG_NAME);
            int patientid = data.getIntExtra(AddPrescription.EXTRA_PATIENT_ID,0);
            String drugform = data.getStringExtra(AddPrescription.EXTRA_DRUG_FORM);
            int drugamount = data.getIntExtra(AddPrescription.EXTRA_DRUG_AMOUNT, 0);
            int druginterval = data.getIntExtra(AddPrescription.EXTRA_DRUG_INTERVAL, 0);
            int totaldays = data.getIntExtra(AddPrescription.EXTRA_TOTAL_DAYS, 0);
            int count = data.getIntExtra(AddPrescription.EXTRA_COUNT, 0);

            PrescriptionEntity prescription = new PrescriptionEntity(patientid,drugname,drugform,druginterval,drugamount,totaldays,count);
            prescriptionViewModel.insert(prescription);
            getActivity().recreate();
            Toast.makeText(getContext(), "Prescription Added", Toast.LENGTH_SHORT).show();


        } else if(requestCode == ADD_USER_REQUEST && resultCode == getActivity().RESULT_OK){
            String fname = data.getStringExtra(UserReg.EXTRA_FNAME);
            String lname = data.getStringExtra(UserReg.EXTRA_LNAME);
            int age = data.getIntExtra(UserReg.EXTRA_AGE,0);
            users.clear();
            userid.clear();
            getActivity().finish();
            startActivity(getActivity().getIntent());
            UserEntity user = new UserEntity(fname,lname,age);
            userViewModel.insert(user);

            Toast.makeText(getContext(), "New User Created", Toast.LENGTH_SHORT).show();

        }else if(requestCode == ADD_PRESCRIPTION_REQUEST && resultCode != getActivity().RESULT_OK) {
            Toast.makeText(getContext(), "Prescription Not Added", Toast.LENGTH_SHORT).show();
        }

        else if(requestCode == ADD_USER_REQUEST && resultCode != getActivity().RESULT_OK) {
            Toast.makeText(getContext(), "No User Created", Toast.LENGTH_SHORT).show();
            getActivity().recreate();
        }


    }

}

//Notification class

//public class NotificationHelper extends ContextWrapper {
//    public static final String channel1ID = "channel1ID";
//    public static final String channel1Name = "Channel 1";
//
//    private  NotificationManager mManager;
//    public NotificationHelper(Context base) {
//
//        super(base);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            createChannels();
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.O)
//    public void createChannels() {
//        NotificationChannel channel1 = new NotificationChannel(channel1ID, channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
//        channel1.enableLights(true);
//        channel1.enableVibration(true);
//        channel1.setLightColor(R.color.colorPrimary);
//        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//        getManager().createNotificationChannel(channel1);
//
//    }
//
//    public NotificationManager getManager(){
//        if(mManager == null){
//            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//
//        return mManager;
//    }
//
//    public NotificationCompat.Builder getChannel1Notification(String title, String message){
//        return new NotificationCompat.Builder(getApplicationContext(), channel1ID )
//                .setContentTitle(title)
//                .setContentText(message)
//                .setSmallIcon(R.drawable.ic_one);
//    }
//
//}

