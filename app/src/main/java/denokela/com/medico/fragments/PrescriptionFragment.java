package denokela.com.medico.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import denokela.com.medico.AlertReceiver;
import denokela.com.medico.activities.AddPrescription;
import denokela.com.medico.activities.MainActivity;
import denokela.com.medico.entities.PrescriptionEntity;
import denokela.com.medico.R;
import denokela.com.medico.entities.UserEntity;
import denokela.com.medico.activities.UserReg;
import denokela.com.medico.adapter.PrescriptionAdapter;
import denokela.com.medico.viewmodels.PrescriptionViewModel;
import denokela.com.medico.viewmodels.UserViewModel;

public class PrescriptionFragment extends Fragment implements View.OnClickListener {
    TextView tvInstruction, tvPrescriptionTitle;
    PrescriptionViewModel prescriptionViewModel;
    UserViewModel userViewModel;
    RecyclerView recyclerView;
    List<String> users = new ArrayList<>();
    List<Integer> userid = new ArrayList<>();
    List<Integer> prescriptionids = new ArrayList<>();
    List<Integer> allprescriptionids = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    FloatingActionButton btnaddPrescription;

    public static final int ADD_PRESCRIPTION_REQUEST = 2;
    public static final int ADD_USER_REQUEST = 3;

    String name = "";

    Integer alertCodes = 0;
    int user = 0;

    PrescriptionAdapter prescriptionAdapter;

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
        if (sharedPref.getInt("RequestCodeTotal", 0) == 0) {
            editor.putInt("RequestCodeTotal", 1);
            editor.apply();
        }
        alertCodes = sharedPref.getInt("RequestCodeTotal", 0);

        tvInstruction = view.findViewById(R.id.tv_instruction);
        tvPrescriptionTitle = view.findViewById(R.id.tv_prescriptiontitle);
        btnaddPrescription = view.findViewById(R.id.btn_add_prescription);
        btnaddPrescription.setOnClickListener(this);


        recyclerView = view.findViewById(R.id.recycler_view_prescription);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        user = sharedPref.getInt("CurrentID", 1);

        name = sharedPref.getString("CurrentName", "No Prescription");
        tvPrescriptionTitle.setText(name);

        prescriptionAdapter = new PrescriptionAdapter();
        recyclerView.setAdapter(prescriptionAdapter);
        userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                users.clear();
                for (int i = 0; i < userEntities.size(); i++) {
                    users.add(userEntities.get(i).getFirstName() + " " + userEntities.get(i).getLastName());
                    userid.add(userEntities.get(i).getUserid());
                }
                users.add("Add a New Patient");

            }
        });


        prescriptionViewModel = ViewModelProviders.of(getActivity()).get(PrescriptionViewModel.class);
        prescriptionViewModel.getCertainPrescription(user).observe(this, new Observer<List<PrescriptionEntity>>() {
            @Override
            public void onChanged(List<PrescriptionEntity> prescriptionEntities) {
                if (prescriptionEntities.size() == 0) {
                    tvInstruction.setVisibility(View.VISIBLE);
                    return;
                }
                for (int i = 0; i < prescriptionEntities.size(); i++) {
                    prescriptionids.add(prescriptionEntities.get(i).getPrescription_Id());
                }

                prescriptionAdapter.setPrescriptions(prescriptionEntities);

            }
        });

        prescriptionViewModel.getAllPrescriptions().observe(this, new Observer<List<PrescriptionEntity>>() {
            @Override
            public void onChanged(List<PrescriptionEntity> prescriptionEntities) {
                for (int i = 0; i < prescriptionEntities.size(); i++) {
                    allprescriptionids.add(prescriptionEntities.get(i).getPrescription_Id());
                }
            }
        });

        //Implement Swipe to delete prescription funtion
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Integer prescription_id = prescriptionAdapter.getPrescriptionAt(viewHolder.getAdapterPosition()).getPrescription_Id();
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity(), AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), prescription_id, intent, 0);
                alarmManager.cancel(pendingIntent);
                prescriptionViewModel.delete(prescriptionAdapter.getPrescriptionAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Prescription Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


    }


    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), String.valueOf(users.size()), Toast.LENGTH_SHORT).show();
        if (users.size() == 1) {
            Intent gotoUserList = new Intent(getContext(), UserReg.class);
            gotoUserList.putExtra("PrescriptionFragment", "pFragment");
            startActivityForResult(gotoUserList, ADD_USER_REQUEST);
            users.clear();
            userid.clear();
        } else {
            Intent intent = new Intent(getContext(), AddPrescription.class);
            startActivityForResult(intent, ADD_PRESCRIPTION_REQUEST);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.prescription_fragment_menu, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.change_user) {
            arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
            arrayAdapter.clear();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select a User");

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
                        gotoUserList.putExtra("PrescriptionFragment", "pFragment");
                        startActivityForResult(gotoUserList, ADD_USER_REQUEST);
                        users.clear();
                        userid.clear();
                        return;
                    }
                    String strname = arrayAdapter.getItem(i);
                    Integer preferenceId = userid.get(i);
                    String preferenceName = users.get(i);
                    editor.putInt("CurrentID", preferenceId);
                    editor.putString("CurrentName", preferenceName);
                    editor.apply();


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
        } else if (item.getItemId() == R.id.delete_all_prescriptions) {
            if (!name.equals("No Prescription") && prescriptionids.size() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("You're about to delete all prescriptions of " + name);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        prescriptionViewModel.deleteAllPrescriptions(sharedPref.getInt("CurrentID", user));

                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getActivity(), AlertReceiver.class);
                        for (int x = 0; x < prescriptionids.size(); x++) {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), prescriptionids.get(x), intent, 0);
                            alarmManager.cancel(pendingIntent);
                        }
                        dialogInterface.dismiss();
                        List<PrescriptionEntity> emptyprescriptions = new ArrayList<>();
                        prescriptionAdapter.setPrescriptions(emptyprescriptions);
                        Toast.makeText(getContext(), "All Prescriptions for this user has been deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            } else if (!name.equals("No Prescription") && prescriptionids.size() == 0) {
                Toast.makeText(getContext(), "No Active Prescriptions found for this user", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No User found, Kindly Register a new user", Toast.LENGTH_SHORT).show();
            }


            return true;
        } else if (item.getItemId() == R.id.delete_all_users) {
            if (!name.equals("No Prescription")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("You're about to delete all Registered Users and their Prescriptions");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getActivity(), AlertReceiver.class);
                        for (int x = 0; x < prescriptionids.size(); x++) {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), allprescriptionids.get(x), intent, 0);
                            alarmManager.cancel(pendingIntent);
                        }
                        userViewModel.deleteAllUsers();
                        prescriptionViewModel.deleteEveryPrescription();

                        dialogInterface.dismiss();
                        name = "No Prescription";
                        tvPrescriptionTitle.setText(name);
                        editor.putString("CurrentName", name);
                        editor.putInt("CurrentID", userid.get(userid.size() - 1) + 1);
                        editor.apply();
                        users.clear();
                        userid.clear();
                        List<PrescriptionEntity> emptyprescriptions = new ArrayList<>();
                        prescriptionAdapter.setPrescriptions(emptyprescriptions);
                        Toast.makeText(getContext(), "Operation Successful", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getContext(), "No User found, Kindly Register a new user", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else if (item.getItemId() == R.id.delete_current_user) {
            if (!name.equals("No Prescription")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("You're about to delete the Current User and their Prescriptions");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getActivity(), AlertReceiver.class);
                        for (int x = 0; x < prescriptionids.size(); x++) {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), prescriptionids.get(x), intent, 0);
                            alarmManager.cancel(pendingIntent);
                        }
                        prescriptionViewModel.deleteAllPrescriptions(sharedPref.getInt("CurrentID", user));
                        userViewModel.delete(sharedPref.getInt("CurrentID", user));
                        if (users.size() > 2) {
                            for (int y = 0; y < userid.size(); y++) {
                                if (userid.get(y).equals(sharedPref.getInt("CurrentID", user))) {
                                    userid.remove(userid.get(y));
                                    users.remove(users.get(y));
                                }
                            }

                            name = users.get(0);
                            tvPrescriptionTitle.setText(name);
                            editor.putInt("CurrentID", userid.get(0));
                            editor.putString("CurrentName", name);
                            editor.apply();
                            getActivity().recreate();

                        } else if (users.size() == 2) {
                            name = "No Prescription";
                            tvPrescriptionTitle.setText(name);
                            editor.putString("CurrentName", name);
                            editor.putInt("CurrentID", userid.get(userid.size() - 1) + 1);
                            editor.apply();
                            List<PrescriptionEntity> emptyprescriptions = new ArrayList<>();
                            prescriptionAdapter.setPrescriptions(emptyprescriptions);
                            getActivity().recreate();
                            users.clear();
                            userid.clear();
                            getActivity().recreate();
                        }
                        dialogInterface.dismiss();

                        Toast.makeText(getContext(), "Operation Successful", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getContext(), "No User found, Kindly Register a new user", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_PRESCRIPTION_REQUEST && resultCode == getActivity().RESULT_OK) {
            String drugname = data.getStringExtra(AddPrescription.EXTRA_DRUG_NAME);
            int patientid = data.getIntExtra(AddPrescription.EXTRA_PATIENT_ID, 0);
            String drugform = data.getStringExtra(AddPrescription.EXTRA_DRUG_FORM);
            int drugamount = data.getIntExtra(AddPrescription.EXTRA_DRUG_AMOUNT, 0);
            int druginterval = data.getIntExtra(AddPrescription.EXTRA_DRUG_INTERVAL, 0);
            int totaldays = data.getIntExtra(AddPrescription.EXTRA_TOTAL_DAYS, 0);
            int count = data.getIntExtra(AddPrescription.EXTRA_COUNT, 0);
            int hour = data.getIntExtra(AddPrescription.ALARM_HOUR, 0);
            int minute = data.getIntExtra(AddPrescription.ALARM_MINUTE, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            PrescriptionEntity prescription = new PrescriptionEntity(patientid, drugname, drugform, druginterval, drugamount, totaldays, count);
            prescriptionViewModel.insert(prescription);
            String key = String.valueOf(alertCodes);

            Intent intent = new Intent(getContext(), AlertReceiver.class);

            String message = "Time to take " + drugname;
            intent.putExtra("key", key);
            intent.putExtra("message", message);
            intent.putExtra("name", name);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), Integer.parseInt(key), intent, 0);

            if (c.before(Calendar.getInstance())) {
                c.add(Calendar.DATE, 1);
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60 * 1000, pendingIntent);
            editor.putInt("RequestCodeTotal", alertCodes + 1).apply();
            getActivity().recreate();

        } else if (requestCode == ADD_USER_REQUEST && resultCode == getActivity().RESULT_OK) {
            String fname = data.getStringExtra(UserReg.EXTRA_FNAME);
            String lname = data.getStringExtra(UserReg.EXTRA_LNAME);
            users.clear();
            userid.clear();
//            getActivity().finish();
//            startActivity(getActivity().getIntent());
            UserEntity user = new UserEntity(fname, lname);
            userViewModel.insert(user);
            if (name.equals("No Prescription")) {
                editor.putString("CurrentName", fname + " " + lname);
                editor.apply();
                name = fname + " " + lname;
                tvPrescriptionTitle.setText(name);
            }

            Toast.makeText(getContext(), "New User Created", Toast.LENGTH_SHORT).show();

        } else if (requestCode == ADD_PRESCRIPTION_REQUEST && resultCode != getActivity().RESULT_OK) {
            Toast.makeText(getContext(), "Prescription Not Added", Toast.LENGTH_SHORT).show();
        } else if (requestCode == ADD_USER_REQUEST && resultCode != getActivity().RESULT_OK) {
            Toast.makeText(getContext(), "No User Created", Toast.LENGTH_SHORT).show();
            getActivity().recreate();
        }


    }

}


