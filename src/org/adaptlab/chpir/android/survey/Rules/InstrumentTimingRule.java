package org.adaptlab.chpir.android.survey.Rules;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Rule;
import org.json.JSONException;

import android.util.Log;

public class InstrumentTimingRule extends PassableRule {
    private static final String TAG = "InstrumentTimingRule";    
    private static final Rule.RuleType RULE_TYPE = Rule.RuleType.INSTRUMENT_TIMING_RULE;
    
    private Instrument mInstrument;
    private Locale mLocale;
    private String mFailureMessage;
    
    public InstrumentTimingRule(Instrument instrument, Locale locale, String failureMessage) {
        mInstrument = instrument;
        mLocale = locale;
        mFailureMessage = failureMessage;
    }
    
    @Override
    public boolean passesRule() {
        if (super.getInstrumentRule(mInstrument, RULE_TYPE) == null) return true;
        
        try {            
            DateFormat timeFormat = new SimpleDateFormat("HH:mm", mLocale);
            
            Date startTime = timeFormat.parse(getInstrumentRule(mInstrument, RULE_TYPE).getParamJSON().getString(Rule.START_TIME_KEY));
            Date endTime = timeFormat.parse(getInstrumentRule(mInstrument, RULE_TYPE).getParamJSON().getString(Rule.END_TIME_KEY));

            return isCurrentTimeInInterval(startTime, endTime, timeFormat);
        } catch (JSONException je) {
            Log.e(TAG, "JSON Exception when parsing JSON for rule: " + je);
            return false;
        } catch (ParseException pe) {
            Log.e(TAG, "Parse Exception when parsing date for rule: " + pe);
            return false;
        }
    }

    @Override
    public String getFailureMessage() {
        return mFailureMessage;
    }
    
    private boolean isCurrentTimeInInterval(Date startTime, Date endTime, DateFormat timeFormat) {        
        try {
            Calendar now = Calendar.getInstance();
            Date currentTime = timeFormat.parse(now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
            return currentTime.after(startTime) && currentTime.before(endTime);
        } catch (ParseException pe) {
            Log.e(TAG, "Error parsing current time: " + pe);
            return false;
        }
    }
}
