package denokela.com.medico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseHolder> {
    private ArrayList<Entries> diseases= new ArrayList<>();
    OnItemClickListener listener;


    @NonNull
    @Override
    public DiseaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_diagnosis_result_items,parent,false);
        return new DiseaseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseHolder holder, int position) {
         holder.tv_diseaseName.setText(diseases.get(position).getName());
         holder.tv_percent.setText(String.valueOf(diseases.get(position).getPercent()) + "%");
    }

    @Override
    public int getItemCount() {
        return diseases.size();
    }

    public void setDiseases(ArrayList<Entries> diseases){
        this.diseases = diseases;
        //this.percent = percent;
        notifyDataSetChanged();
    }

    class DiseaseHolder extends RecyclerView.ViewHolder{
        private TextView tv_diseaseName, tv_percent;
        public DiseaseHolder(@NonNull View itemView) {
            super(itemView);
            tv_diseaseName = itemView.findViewById(R.id.tv_diseaseName);
            tv_percent = itemView.findViewById(R.id.tv_diseasePercent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener !=null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(diseases.get(position));
                    }
                }
            });
        }
    }


    public interface  OnItemClickListener{
        void onItemClick(Entries entries);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
