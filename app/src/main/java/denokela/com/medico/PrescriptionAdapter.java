package denokela.com.medico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionHolder>{

    private List<PrescriptionEntity> prescriptions = new ArrayList<>();

    @NonNull
    @Override
    public PrescriptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_prescription_items,parent,false);
        return new PrescriptionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionHolder holder, int position) {
        PrescriptionEntity currentPrescriptions = prescriptions.get(position);
        String form="";
        if(currentPrescriptions.getDrugForm().equals("Tablet")){
            form = "Tablet";
        }else if(currentPrescriptions.getDrugForm().equals("Syrup")){
            form ="Spoon";
        }else if(currentPrescriptions.getDrugForm().equals("Injection")){
            form = "Injection";
        }

        holder.tv_DrugName.setText(currentPrescriptions.getDrugName());
        holder.tv_Drugform.setText(currentPrescriptions.getDrugForm());
        holder.tvDrugInstruction.setText(String.valueOf(currentPrescriptions.getDoseNumber()) + " " + form + " after every "+String.valueOf(currentPrescriptions.getDoseInterval() + " hour"));
        holder.tvDrugAmount.setText("Doses Left: "+ String.valueOf(currentPrescriptions.getCount()));
    }

    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    public void setPrescriptions(List<PrescriptionEntity> prescriptions){
        this.prescriptions = prescriptions;
        notifyDataSetChanged();
    }

    class PrescriptionHolder extends RecyclerView.ViewHolder{
        private TextView tv_Drugform,tv_DrugName,tvDrugInstruction,tvDrugAmount;

        public PrescriptionHolder(@NonNull View itemView) {
            super(itemView);
            tv_Drugform = itemView.findViewById(R.id.tv_drugform);
            tv_DrugName = itemView.findViewById(R.id.tv_drugname);
            tvDrugAmount = itemView.findViewById(R.id.tv_drugamount);
            tvDrugInstruction = itemView.findViewById(R.id.tv_druginstruction);
        }
    }
}
