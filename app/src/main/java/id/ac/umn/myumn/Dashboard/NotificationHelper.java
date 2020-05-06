package id.ac.umn.myumn.Dashboard;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import id.ac.umn.myumn.Notification;
import id.ac.umn.myumn.R;


public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    public static final String test = "test";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String a) {
        Intent intent = new Intent(this, Notification.class);
        intent.putExtra("week1", channelID);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(a)
                .setContentText("Your AlarmManager is working.")
                .setSmallIcon(R.drawable.arrow_down)
                .setContentIntent(pIntent);
    }
}