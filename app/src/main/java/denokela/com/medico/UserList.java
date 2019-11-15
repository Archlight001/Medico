package denokela.com.medico;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView listView;
    Button btnCreateUser;
    List<User> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listView = findViewById(R.id.userListView);
        btnCreateUser = findViewById(R.id.btnCreateUser);

        final UserList useractivity = (UserList)this;

        btnCreateUser.setOnClickListener(this);
        ExecDatabase eDatabase = new ExecDatabase(new ExecDatabase.AsyncRespone() {
            @Override
            public void processfinish(List output) {
                Toast.makeText(getApplicationContext(),"Ive reached here",Toast.LENGTH_SHORT).show();
                if(!output.isEmpty()){
                    data = new ArrayList<>(output);
                    String[] names = new String[data.size()];
                    for(int i=0;i<names.length;i++){
                        names[i]=data.get(i).getFirstName() + " " + data.get(i).getLastName();
                    }
                    ListAdapter listAdapter = new ArrayAdapter<String>(useractivity,android.R.layout.simple_list_item_1,names);
                    listView.setAdapter(listAdapter);

                }else{
                    Toast.makeText(getApplicationContext(),"List is Empty",Toast.LENGTH_LONG).show();
                }

            }
        },"readData",getApplicationContext());
        eDatabase.execute();

    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(),UserReg.class));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
