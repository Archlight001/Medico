package denokela.com.medico;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import denokela.com.medico.activities.MainActivity;

public class NotificationActivity extends AppCompatActivity {
    Intent intent;
    int count=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Intent intentVals = getIntent();

        intent = new Intent(this, MainActivity.class);
        showDialog();
    }



    @SuppressLint("ResourceAsColor")
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
        builder.setMessage("\n" + "This is the Activity");
        TextView title = new TextView(NotificationActivity.this);
        title.setText("REMINDER!!!!!!");
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(20, 20, 20, 20);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        builder.setCustomTitle(title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                count-=1;
                                Bundle bundle = new Bundle();
                                String strCount= String.valueOf(count);
                                bundle.putString("value",strCount);
                                intent.putExtras(bundle);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                finish();

                            }
                        });
        builder.show();

    }
}
