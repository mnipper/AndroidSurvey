package org.adaptlab.chpir.android.survey.Receivers;

import org.adaptlab.chpir.android.survey.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    public final static String EXTRA_NOTIFICATION_TEXT = 
            "org.adaptlab.chpir.android.survey.Receivers.notification_text";
    private static final String TAG = "NotificationReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationText = intent.getStringExtra(EXTRA_NOTIFICATION_TEXT);
        Log.i(TAG, "Received notification: " + notificationText);
        
        Resources r = context.getResources();

        Notification notification = new NotificationCompat.Builder(context)
            .setTicker(r.getString(R.string.app_name))
            .setContentTitle(r.getString(R.string.app_name))
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentText(notificationText)
            .setAutoCancel(true)
            .build();
        
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        notificationManager.notify(0, notification);
    }
}
