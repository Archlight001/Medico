package denokela.com.medico;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PrescriptionFragment extends Fragment implements View.OnClickListener {
    TextView tvInstruction;
    PrescriptionViewModel prescriptionViewModel;
    UserViewModel userViewModel;
    RecyclerView recyclerView;

    FloatingActionButton btnaddPrescription;

    public static final int ADD_PRESCRIPTION_REQUEST=2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prescription,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvInstruction= view.findViewById(R.id.tv_instruction);
        btnaddPrescription = view.findViewById(R.id.btn_add_prescription);
        btnaddPrescription.setOnClickListener(this);


        recyclerView = view.findViewById(R.id.recycler_view_prescription);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final PrescriptionAdapter prescriptionAdapter = new PrescriptionAdapter();
        recyclerView.setAdapter(prescriptionAdapter);
         userViewModel= ViewModelProviders.of(getActivity()).get(UserViewModel.class);
         userViewModel.getAllUsers().observe(this, new Observer<List<UserEntity>>() {
             @Override
             public void onChanged(List<UserEntity> userEntities) {
                 Toast.makeText(getContext(), userEntities.get(0).getFirstName(), Toast.LENGTH_SHORT).show();
             }
         });


        prescriptionViewModel= ViewModelProviders.of(getActivity()).get(PrescriptionViewModel.class);
        prescriptionViewModel.getAllPrescriptions().observe(this, new Observer<List<PrescriptionEntity>>() {
            @Override
            public void onChanged(List<PrescriptionEntity> prescriptionEntities) {
                if(prescriptionEntities.size()==0){
                    tvInstruction.setVisibility(View.VISIBLE);
                    return;
                }

                prescriptionAdapter.setPrescriptions(prescriptionEntities);

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(),AddPrescription.class);
        startActivityForResult(intent,ADD_PRESCRIPTION_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_PRESCRIPTION_REQUEST && resultCode == getActivity().RESULT_OK){
            String drugname = data.getStringExtra(AddPrescription.EXTRA_DRUG_NAME);
            String patientname = data.getStringExtra(AddPrescription.EXTRA_PATIENT_NAME);
            String drugform = data.getStringExtra(AddPrescription.EXTRA_DRUG_FORM);
            int drugamount = data.getIntExtra(AddPrescription.EXTRA_DRUG_AMOUNT,0);
            int druginterval = data.getIntExtra(AddPrescription.EXTRA_DRUG_INTERVAL,0);
            int totaldays = data.getIntExtra(AddPrescription.EXTRA_TOTAL_DAYS,0);
            int count = data.getIntExtra(AddPrescription.EXTRA_COUNT,0);

            PrescriptionEntity prescription = new PrescriptionEntity(patientname,drugname,drugform,druginterval,drugamount,totaldays,count);
            prescriptionViewModel.insert(prescription);

            Toast.makeText(getContext(), "Prescription Added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Prescription Not Added", Toast.LENGTH_SHORT).show();
        }
    }
}
