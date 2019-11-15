package denokela.com.medico;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeScreen extends Fragment implements View.OnClickListener {
    Button btnDiagnosis;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homescreen_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDiagnosis = view.findViewById(R.id.btnDiagnose);
        btnDiagnosis.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getContext(),UserList.class));

    }
}
