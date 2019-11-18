package denokela.com.medico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<UserEntity> users = new ArrayList<>();

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_items, parent,false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        UserEntity currentUser = users.get(position);
        holder.textView_name.setText(currentUser.getFirstName() + "  " + currentUser.getLastName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<UserEntity> users){
        this.users = users;
        notifyDataSetChanged();
    }

    class UserHolder extends RecyclerView.ViewHolder{
        private TextView textView_name;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            textView_name= itemView.findViewById(R.id.tv_name);
        }
    }
}
