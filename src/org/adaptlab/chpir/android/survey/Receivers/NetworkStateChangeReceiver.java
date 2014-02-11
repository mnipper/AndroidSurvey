package org.adaptlab.chpir.android.survey.Receivers;

import org.adaptlab.chpir.android.survey.Tasks.SendResponsesTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkStateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new SendResponsesTask(context).execute();
    }
}
