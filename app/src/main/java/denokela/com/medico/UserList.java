package denokela.com.medico;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView listView;
    Button btnCreateUser;

    public static final int ADD_USER_REQUEST=1;


    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final UserAdapter adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        btnCreateUser = findViewById(R.id.btnCreateUser);

        final UserList useractivity = (UserList)this;

        btnCreateUser.setOnClickListener(useractivity);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                //update Recycler View
                adapter.setUsers(userEntities);
            }
        });


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(UserList.this, UserReg.class);
        startActivityForResult(intent,ADD_USER_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_USER_REQUEST && resultCode == RESULT_OK){
            String fname = data.getStringExtra(UserReg.EXTRA_FNAME);
            String lname = data.getStringExtra(UserReg.EXTRA_LNAME);
            int age = data.getIntExtra(UserReg.EXTRA_AGE,0);

            UserEntity user = new UserEntity(fname,lname,age);
            userViewModel.insert(user);

            Toast.makeText(this, "New User Created", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "No User Created", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
