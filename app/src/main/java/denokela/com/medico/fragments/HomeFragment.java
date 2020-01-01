package denokela.com.medico.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import denokela.com.medico.R;
import denokela.com.medico.activities.Diagnosis_questions;

public class HomeFragment extends Fragment implements View.OnClickListener {
    Button btnDiagnosis;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homefragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        btnDiagnosis = view.findViewById(R.id.btnDiagnose);
        progressDialog.setMessage("Processing...");
        btnDiagnosis.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), "Starting Diagnosis", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getContext(), Diagnosis_questions.class));
    }
}
