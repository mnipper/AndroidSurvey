package org.adaptlab.chpir.android.activerecordcloudsync;

import org.adaptlab.chpir.android.survey.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NetworkNotificationUtils {
    private static final String TAG = "NetworkNotificationUtils";
    

    /*
     * Place a notification in the notification tray
     */
    public static void showNotification(Context context, int iconId, int textId) {
        if (context == null) return;
        
        Resources r = context.getResources();

        Notification notification = new NotificationCompat.Builder(context)
            .setTicker(r.getString(R.string.app_name))
            .setSmallIcon(iconId)
            .setContentTitle(r.getString(R.string.app_name))
            .setContentText(r.getString(textId))
            .setAutoCancel(true)
            .build();
        
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);;
        
        notificationManager.notify(0, notification);
    }
    
    /*
     * Check for various network errors and display error message in notification
     * tray.
     * 
     * Return false if network errors, return true if okay to proceed.
     * 
     * This WILL throw an exception if executed on UI thread since it pings a URL.
     */
    public static boolean checkForNetworkErrors(Context context) {
        if (!isNetworkAvailable(context.getApplicationContext())) {
            Log.i(TAG, "Network is not available, short circuiting PollService...");
            showNotification(context, android.R.drawable.ic_dialog_alert, R.string.network_unavailable);
        } else if (!ActiveRecordCloudSync.isApiAvailable()) {
            Log.i(TAG, "Api endpoint is not available, short circuiting PollService...");
            showNotification(context, android.R.drawable.ic_dialog_alert, R.string.api_unavailable);
        } else if (!ActiveRecordCloudSync.isVersionAcceptable()) {
            Log.i(TAG, "Android version code is not acceptable, short circuting PollService...");
            showNotification(context, android.R.drawable.ic_dialog_alert, R.string.unacceptable_version_code);
        } else {
            return true;
        }

        return false;
    }
    
    /*
     * Check if Network is available on device.
     */
    @SuppressWarnings("deprecation")
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;
    }
}
