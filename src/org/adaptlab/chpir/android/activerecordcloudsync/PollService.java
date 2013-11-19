package org.adaptlab.chpir.android.activerecordcloudsync;

import java.util.Date;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static int DEFAULT_POLL_INTERVAL = 1000 * 15;
    private static int sPollInterval;
    private static Date lastUpdate;

    public PollService() {
        super(TAG);
        sPollInterval = DEFAULT_POLL_INTERVAL;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        boolean isNetworkAvailable = cm.getBackgroundDataSetting() &&
            cm.getActiveNetworkInfo() != null;
        
        if (!isNetworkAvailable) { 
            Log.i(TAG, "Network is not available, short circuiting PollService...");
        } else if (!ActiveRecordCloudSync.isApiAvailable()) {
            Log.i(TAG, "Api endpoint is not available, short circuiting PollService...");
        } else {
            lastUpdate = new Date();
            ActiveRecordCloudSync.syncReceiveTables();
            ActiveRecordCloudSync.syncSendTables();
        }
    }
    
    // Control polling of api, set isOn to true to enable polling
    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(
            context, 0, i, 0);
        
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC,
                    System.currentTimeMillis(), sPollInterval, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
    
    public static boolean isServiceAlarmOn(Context context) {
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
    
    public static void setPollInterval(int interval) {
            sPollInterval = interval;
    }
    
    public static void restartServiceAlarm(Context context) {
        setServiceAlarm(context, false);
        setServiceAlarm(context, true);
    }
    
    public static Date getLastUpdate() {
        return lastUpdate;
    }
}
