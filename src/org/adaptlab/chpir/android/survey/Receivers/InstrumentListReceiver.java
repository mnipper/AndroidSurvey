package org.adaptlab.chpir.android.survey.Receivers;

import org.adaptlab.chpir.android.survey.Models.Instrument;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InstrumentListReceiver extends BroadcastReceiver {

    private static final String TAG = "InstrumentListReceiver";
    private static final String INSTRUMENT_LIST = "org.adaptlab.chpir.android.survey.instrument_list";
    private static final String INSTRUMENT_TITLE_LIST = "org.adaptlab.chpir.android.survey.instrument_title_list";
    private static final String INSTRUMENT_ID_LIST = "org.adaptlab.chpir.android.survey.instrument_id_list";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast to send list of available instruments");
        
        int instrumentListSize = Instrument.getAll().size();
        
        String[] instrumentTitleList = new String[instrumentListSize];
        long[] instrumentIdList = new long[instrumentListSize];

        for (int i = 0; i < instrumentListSize; i++) {
            instrumentTitleList[i] = Instrument.getAll().get(i).getTitle();
            instrumentIdList[i] = Instrument.getAll().get(i).getRemoteId();
        }
        
        Intent i = new Intent();
        i.setAction(INSTRUMENT_LIST);
        i.putExtra(INSTRUMENT_TITLE_LIST, instrumentTitleList);
        i.putExtra(INSTRUMENT_ID_LIST, instrumentIdList);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.sendBroadcast(i);
    }
}
