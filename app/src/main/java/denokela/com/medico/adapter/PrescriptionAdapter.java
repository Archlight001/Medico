package denokela.com.medico.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import denokela.com.medico.entities.PrescriptionEntity;
import denokela.com.medico.R;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionHolder> {

    private List<PrescriptionEntity> prescriptions = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public PrescriptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_prescription_items, parent, false);
        return new PrescriptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionHolder holder, int position) {
        PrescriptionEntity currentPrescriptions = prescriptions.get(position);
        String form = "";
        if (currentPrescriptions.getDrugForm().equals("Tablet")) {
            form = "Tablet";
        } else if (currentPrescriptions.getDrugForm().equals("Syrup")) {
            form = "Spoon";
        } else if (currentPrescriptions.getDrugForm().equals("Injection")) {
            form = "Injection";
        }

        holder.tv_DrugName.setText(currentPrescriptions.getDrugName());
        holder.tv_Drugform.setText(currentPrescriptions.getDrugForm());
        holder.tvDrugInstruction.setText(String.valueOf(currentPrescriptions.getDoseNumber()) + " " + form + " after every " + String.valueOf(currentPrescriptions.getDoseInterval() + " hour"));
        holder.tvDrugAmount.setText("Doses Left: " + String.valueOf(currentPrescriptions.getCount()));
    }

    public interface onItemClickListener {
        void onItemClick(PrescriptionEntity prescriptionEntity);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    public void setPrescriptions(List<PrescriptionEntity> prescriptions) {
        this.prescriptions = prescriptions;
        notifyDataSetChanged();
    }

    public PrescriptionEntity getPrescriptionAt(int position) {
        return prescriptions.get(position);
    }

    class PrescriptionHolder extends RecyclerView.ViewHolder {
        private TextView tv_Drugform, tv_DrugName, tvDrugInstruction, tvDrugAmount;

        public PrescriptionHolder(@NonNull View itemView) {
            super(itemView);
            tv_Drugform = itemView.findViewById(R.id.tv_drugform);
            tv_DrugName = itemView.findViewById(R.id.tv_drugname);
            tvDrugAmount = itemView.findViewById(R.id.tv_drugamount);
            tvDrugInstruction = itemView.findViewById(R.id.tv_druginstruction);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(prescriptions.get(position));
                    }
                }
            });
        }
    }
}
