package org.adaptlab.chpir.android.survey.Receivers;

import org.adaptlab.chpir.android.survey.SurveyActivity;
import org.adaptlab.chpir.android.survey.SurveyFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SurveyLaunchReceiver extends BroadcastReceiver {
    private static final String TAG = "SurveyLaunchReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Long instrumentId = intent.getLongExtra(SurveyFragment.EXTRA_INSTRUMENT_ID, -1);
        String metadata = intent.getStringExtra(SurveyFragment.EXTRA_PARTICIPANT_METADATA);
        
        Log.i(TAG, "Received broadcast to launch survey for instrument with remote id " + instrumentId
                + " and metadata " + metadata);
        
        if (instrumentId > -1) {
            Intent i = new Intent(context, SurveyActivity.class);
            i.putExtra(SurveyFragment.EXTRA_INSTRUMENT_ID, instrumentId);
            i.putExtra(SurveyFragment.EXTRA_PARTICIPANT_METADATA, metadata);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(i);
        }
    }
}