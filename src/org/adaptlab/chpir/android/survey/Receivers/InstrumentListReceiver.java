package org.adaptlab.chpir.android.survey.Receivers;

import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Rule;
import org.adaptlab.chpir.android.survey.Models.Rule.RuleType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InstrumentListReceiver extends BroadcastReceiver {

    private static final String TAG = "InstrumentListReceiver";
    private static final String INSTRUMENT_LIST = "org.adaptlab.chpir.android.survey.instrument_list";
    private static final String INSTRUMENT_TITLE_LIST = "org.adaptlab.chpir.android.survey.instrument_title_list";
    private static final String INSTRUMENT_ID_LIST = "org.adaptlab.chpir.android.survey.instrument_id_list";
    private static final String INSTRUMENT_PARTICIPANT_TYPE = "org.adaptlab.chpir.android.survey.instrument_participant_type";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast to send list of available instruments");
        
        Long currentProjectId;
        
        try {
            currentProjectId = Long.valueOf(AdminSettings.getInstance().getProjectId());
        } catch (NumberFormatException nfe) {
            Log.e(TAG, "Project ID is not a number: " + nfe);
            return;
        }
        
        int instrumentListSize = Instrument.getAllProjectInstruments(currentProjectId).size();
        
        String[] instrumentTitleList = new String[instrumentListSize];
        long[] instrumentIdList = new long[instrumentListSize];
        String[] instrumentParticipantTypes = new String[instrumentListSize];

        for (int i = 0; i < instrumentListSize; i++) {
            instrumentTitleList[i] = Instrument.getAllProjectInstruments(currentProjectId).get(i).getTitle();
            instrumentIdList[i] = Instrument.getAllProjectInstruments(currentProjectId).get(i).getRemoteId();
            Rule rules = Rule.findByRuleTypeAndInstrument(RuleType.PARTICIPANT_TYPE_RULE, Instrument.getAllProjectInstruments(currentProjectId).get(i));
            if (rules == null) {
                instrumentParticipantTypes[i] = "";
            } else {
                instrumentParticipantTypes[i] = rules.getParamJSON().toString();
            }
        }
        
        Intent i = new Intent();
        i.setAction(INSTRUMENT_LIST);
        i.putExtra(INSTRUMENT_TITLE_LIST, instrumentTitleList);
        i.putExtra(INSTRUMENT_ID_LIST, instrumentIdList);
        i.putExtra(INSTRUMENT_PARTICIPANT_TYPE, instrumentParticipantTypes);
        context.sendBroadcast(i);
    }
}
