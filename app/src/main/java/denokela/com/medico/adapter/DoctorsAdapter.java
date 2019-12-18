package denokela.com.medico.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import denokela.com.medico.R;
import denokela.com.medico.entities.DoctorsEntity;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorsHolder> {

    private List<DoctorsEntity> doctors = new ArrayList<>();
    OnItemClickListener listener;
    Context context;

    @NonNull
    @Override
    public DoctorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_doctors,parent,false);
        return new DoctorsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsHolder holder, int position) {
        holder.tv_doctorsName.setText(doctors.get(position).getDoctorName());
        holder.tv_doctorsAddress.setText(doctors.get(position).getDoctorAddress());
        holder.tv_doctorsEmail.setText(doctors.get(position).getDoctorsEmail());
        holder.tv_doctorsPhoneNumber.setText(doctors.get(position).getDoctorsPhoneNumber());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Once call button is clicked send number to call app for call
                Uri u = Uri.parse("tel:"+doctors.get(position).getDoctorsPhoneNumber());
                try{
                    context.startActivity(new Intent(Intent.ACTION_DIAL,u));
                }catch (SecurityException e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    class DoctorsHolder extends RecyclerView.ViewHolder{
        private TextView tv_doctorsName, tv_doctorsAddress,tv_doctorsEmail, tv_doctorsPhoneNumber;
        private ImageButton imageButton;
        public DoctorsHolder(@NonNull View itemView) {
            super(itemView);
            tv_doctorsName = itemView.findViewById(R.id.tv_doctorName);
            tv_doctorsAddress = itemView.findViewById(R.id.tv_doctorAddress);
            tv_doctorsEmail = itemView.findViewById(R.id.tv_doctorEmail);
            tv_doctorsPhoneNumber = itemView.findViewById(R.id.tv_doctorPhoneNumber);
            imageButton = itemView.findViewById(R.id.image_button_call);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(doctors.get(position).getDoctorAddress());
                    }
                }
            });
        }
    }

    public void setDoctors(List<DoctorsEntity> doctors,Context context){
        this.doctors = doctors;
        this.context = context;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(String address);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


}
