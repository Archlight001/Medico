package denokela.com.medico;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {


    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String key = intent.getStringExtra("key");
        String message = intent.getStringExtra("message");
        String name = intent.getStringExtra("name");
        NotificationHelper notificationHelper = new NotificationHelper(context, key,name, message);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(Integer.parseInt(key), nb.build());

    }


}
