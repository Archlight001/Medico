package denokela.com.medico;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import denokela.com.medico.activities.MainActivity;


public class NotificationHelper extends ContextWrapper {

    String channelid;
    String messageBody;
    String name;

    private NotificationManager mManager;

    public NotificationHelper(Context base, String channelid,String name, String messageBody) {
        super(base);

        this.channelid = channelid;
        this.messageBody = messageBody;
        this.name=name;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel = new NotificationChannel(channelid, "Medico" + channelid, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }


    public NotificationCompat.Builder getChannelNotification() {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("key", channelid);
        intent.putExtra("name",name);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(channelid), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), channelid)
                .setContentTitle("Prescription Reminder")
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.ic_casino)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }
}
