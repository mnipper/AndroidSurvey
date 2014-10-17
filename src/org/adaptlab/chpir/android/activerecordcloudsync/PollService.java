package org.adaptlab.chpir.android.activerecordcloudsync;

import java.util.Date;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static int DEFAULT_POLL_INTERVAL = 1000 * 120;
    public static final String PREF_IS_ALARM_ON = "isAlarmOn";
    private static int sPollInterval;
    private static Date lastUpdate;

    public PollService() {
        super(TAG);
        sPollInterval = DEFAULT_POLL_INTERVAL;
    }
    

    @Override
    protected void onHandleIntent(Intent intent) {
        if (NetworkNotificationUtils.checkForNetworkErrors(getApplicationContext())) {
            lastUpdate = new Date();
            ActiveRecordCloudSync.syncSendTables(getApplicationContext());
        }
    }

    // Control polling of api, set isOn to true to enable polling
    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC,
                    System.currentTimeMillis(), sPollInterval, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(PollService.PREF_IS_ALARM_ON, isOn)
            .commit();
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i,
                PendingIntent.FLAG_NO_CREATE);
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
